package com.andreitech.sims;

import com.andreitech.sims.entity.User;
import com.andreitech.sims.entity.products.Food;
import com.andreitech.sims.entity.products.Medication;
import com.andreitech.sims.repository.UserRepository;
import com.andreitech.sims.repository.products.FoodRepository;
import com.andreitech.sims.repository.products.MedicationRepository;
import com.andreitech.sims.service.EmailService;
import com.andreitech.sims.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.util.*;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class NotificationServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private NotificationService notificationService;

    private User user;
    private Food food;
    private Medication medication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock User
        user = new User();
        user.setId(1L);
        user.setEmail("testuser@example.com");

        // Mock Food
        food = new Food();
        food.setId(1L);
        food.setCategory("Food");
        food.setName("Milk");
        Date expiryDate = new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000); // Expires in 3 days
        food.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        food.setUser(user);

        // Mock Medication
        medication = new Medication();
        medication.setId(1L);
        medication.setCategory("Medication");
        medication.setName("Aspirin");
        expiryDate = new Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000); // Expired 2 days ago
        medication.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        medication.setUser(user);
    }

    @Test
    void testSendExpireNotificationsForFood() {
        // Arrange
        when(foodRepository.findAll()).thenReturn(List.of(food));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        notificationService.sendExpireNotifications();

        // Assert
        verify(emailService, times(1)).sendEmail(
                eq(user.getEmail()),
                eq("Food Expiry Daily Notification"),
                contains("Milk will expire in 3 days")
        );
    }

    @Test
    void testSendExpireNotificationsForMedication() {
        // Arrange
        when(medicationRepository.findAll()).thenReturn(List.of(medication));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        notificationService.sendExpireNotifications();

        // Assert
        verify(emailService, times(1)).sendEmail(
                eq(user.getEmail()),
                eq("Medication Expiry Daily Notification"),
                contains("Aspirin expired 2 days ago")
        );
    }

    @Test
    void testSendExpireNotificationsNoExpiringProducts() {
        // Arrange
        when(foodRepository.findAll()).thenReturn(Collections.emptyList());
        when(medicationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        notificationService.sendExpireNotifications();

        // Assert
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSendExpireNotificationsMultipleProducts() {
        // Arrange
        Food anotherFood = new Food();
        anotherFood.setId(2L);
        anotherFood.setName("Cheese");
        Date expiryDate = new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000);  // Expires in 5 days
        anotherFood.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        anotherFood.setUser(user);

        when(foodRepository.findAll()).thenReturn(List.of(food, anotherFood));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        notificationService.sendExpireNotifications();

        // Assert
        verify(emailService, times(1)).sendEmail(
                eq(user.getEmail()),
                eq("Food Expiry Daily Notification"),
                argThat(argument ->
                        allOf(
                                containsString("Milk will expire in 3 days"),
                                containsString("Cheese will expire in 5 days")
                        ).matches(argument))
        );
    }

    @Test
    void testSendExpireNotificationsWithMissingUser() {
        // Arrange
        when(foodRepository.findAll()).thenReturn(List.of(food));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> notificationService.sendExpireNotifications());
        assertEquals("User not found", exception.getMessage());

        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }
}
