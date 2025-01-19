//package com.andreitech.sims;
//
//import com.andreitech.sims.entity.products.Food;
//import com.andreitech.sims.entity.products.Medication;
//import com.andreitech.sims.repository.UserRepository;
//import com.andreitech.sims.repository.products.FoodRepository;
//import com.andreitech.sims.repository.products.MedicationRepository;
//import com.andreitech.sims.entity.User;
//import com.andreitech.sims.service.EmailService;
//import com.andreitech.sims.service.NotificationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.*;
//
//import static org.mockito.Mockito.*;
//
//public class NotificationServiceTests {
//
//    @Mock
//    private FoodRepository foodRepository;
//
//    @Mock
//    private MedicationRepository medicationRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private EmailService emailService;
//
//    @InjectMocks
//    private NotificationService notificationService;
//
//    private User user;
//    private Food food;
//    private Medication medication;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//
//        // Setup mock user
//        user = new User();
//        user.setId(1L);
//        user.setEmail("user@example.com");
//
//        // Setup mock Food and Medication
//        food = new Food();
//        food.setId(1L);
//        food.setName("Bread");
//        food.setUser(user);
//        food.setExpiryDate(new Date(System.currentTimeMillis() + 1000000L));  // Expiry in the future
//
//        medication = new Medication();
//        medication.setId(1L);
//        medication.setName("Aspirin");
//        medication.setUser(user);
//        medication.setExpiryDate(new Date(System.currentTimeMillis() - 1000000L));  // Expired
//    }
//
//    @Test
//    public void testSendExpireNotificationsForFood() {
//        // Arrange: Mock the repository to return a list of food products
//        List<Food> foods = Collections.singletonList(food);
//        when(foodRepository.findAll()).thenReturn(foods);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // Act: Call the notification method
//        notificationService.sendExpireNotifications();
//
//        // Assert: Verify that emailService.sendEmail was called
//        verify(emailService, times(1)).sendEmail(eq("user@example.com"), eq("Food Expiry Daily Notification"), contains("Bread"));
//    }
//
//    @Test
//    public void testSendExpireNotificationsForMedication() {
//        // Arrange: Mock the repository to return a list of medications
//        List<Medication> medications = Collections.singletonList(medication);
//        when(medicationRepository.findAll()).thenReturn(medications);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // Act: Call the notification method
//        notificationService.sendExpireNotifications();
//
//        // Assert: Verify that emailService.sendEmail was called
//        verify(emailService, times(1)).sendEmail(eq("user@example.com"), eq("Medication Expiry Daily Notification"), contains("Aspirin"));
//    }
//
//    @Test
//    public void testSendExpireNotificationsNoExpiringProducts() {
//        // Arrange: Mock repository to return an empty list (no products expiring)
//        when(foodRepository.findAll()).thenReturn(new ArrayList<>());
//        when(medicationRepository.findAll()).thenReturn(new ArrayList<>());
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // Act: Call the notification method
//        notificationService.sendExpireNotifications();
//
//        // Assert: Verify that emailService.sendEmail was not called
//        verify(emailService, never()).sendEmail(any(), any(), any());
//    }
//
//    @Test
//    public void testSendExpireNotificationsWithExpiredProducts() {
//        // Arrange: Mock the repository to return both expired and non-expired products
//        List<Food> foods = Collections.singletonList(food);
//        List<Medication> medications = Collections.singletonList(medication);
//        when(foodRepository.findAll()).thenReturn(foods);
//        when(medicationRepository.findAll()).thenReturn(medications);
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//
//        // Act: Call the notification method
//        notificationService.sendExpireNotifications();
//
//        // Assert: Verify email was sent for both food and medication
//        verify(emailService, times(1)).sendEmail(eq("user@example.com"), eq("Food Expiry Daily Notification"), contains("Bread"));
//        verify(emailService, times(1)).sendEmail(eq("user@example.com"), eq("Medication Expiry Daily Notification"), contains("Aspirin"));
//    }
//}
