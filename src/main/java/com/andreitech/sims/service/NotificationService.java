package com.andreitech.sims.service;

import com.andreitech.sims.entity.Product;
import com.andreitech.sims.entity.User;
import com.andreitech.sims.entity.products.Food;
import com.andreitech.sims.entity.products.Medication;
import com.andreitech.sims.repository.UserRepository;
import com.andreitech.sims.repository.products.FoodRepository;
import com.andreitech.sims.repository.products.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public class NotificationService {
    private final FoodRepository foodRepository;
    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // Dependency Injection
    @Autowired
    public NotificationService(FoodRepository foodRepository, MedicationRepository medicationRepository, UserRepository userRepository, EmailService emailService) {
        this.foodRepository = foodRepository;
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // Send scheduled notifications for products that are about to expire
    @Scheduled(cron = "0 0 9 * * ?")
    @Async
    public void sendExpireNotifications() {
        sendExpireNotifications(foodRepository, Food::calculateDaysUntilExpiry, Food::getDaysUntilExpiry, "Food Expiry Daily Notification");
        sendExpireNotifications(medicationRepository, Medication::calculateDaysUntilExpiry, Medication::getDaysUntilExpiry, "Medication Expiry Daily Notification");
    }

    @Async
    protected <T extends Product> void sendExpireNotifications(
            JpaRepository<T, Long> productRepository,
            Consumer<T> calculateDaysUntilExpiry,
            Function<T, Long> getDaysUntilExpiry,
            String emailSubject
    ) {
        List<T> products = productRepository.findAll();

        // Create a dictionary (Map) where each user has a list of products about to expire
        Map<Long, List<T>> expiryProductsMap = new HashMap<>();

        // Populate the dictionary with product expiry details
        for (T product : products) {
            // Calculate how many days until expiry using the provided function
            calculateDaysUntilExpiry.accept(product);

            // Only store products expiring in 7 or fewer days
            if (getDaysUntilExpiry.apply(product) <= 7) {
                expiryProductsMap.computeIfAbsent(product.getUser().getId(), _ -> new ArrayList<>()).add(product);
            }
        }

        // For each user, send a single email with the list of expiring products
        for (Map.Entry<Long, List<T>> entry : expiryProductsMap.entrySet()) {
            Long userId = entry.getKey();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

            List<T> productList = entry.getValue();

            // Split the products into two lists and sort them
            List<T> positiveList = productList.stream()
                    .filter(product -> getDaysUntilExpiry.apply(product) >= 0)
                    .sorted(Comparator.comparingLong(getDaysUntilExpiry::apply))
                    .toList();

            List<T> negativeList = productList.stream()
                    .filter(product -> getDaysUntilExpiry.apply(product) < 0)
                    .sorted(Comparator.comparingLong(getDaysUntilExpiry::apply))
                    .toList();

            // Build the email message
            StringBuilder message = new StringBuilder("The following " +  products.getFirst().getCategory().toLowerCase() + " are about to expire in the next 7 days:\n");

            for (T productInfo : positiveList) {
                message.append("- ").append(productInfo.getName())
                        .append(" will expire in ").append(getDaysUntilExpiry.apply(productInfo))
                        .append(" days.\n");
            }

            message.append("\n\nThe following ").append(products.getFirst().getCategory().toLowerCase()).append(" have already expired:\n");
            for (T productInfo : negativeList) {
                message.append("- ").append(productInfo.getName())
                        .append(" expired ").append(-getDaysUntilExpiry.apply(productInfo))
                        .append(" days ago.\n");
            }

            // Send the email to the user
            emailService.sendEmail(user.getEmail(), emailSubject, message.toString());
        }
    }
}
