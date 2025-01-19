package com.andreitech.sims.products;

import com.andreitech.sims.entity.User;
import com.andreitech.sims.entity.products.Food;
import com.andreitech.sims.repository.UserRepository;
import com.andreitech.sims.repository.products.FoodRepository;
import com.andreitech.sims.service.products.FoodService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FoodService foodService;

    private User user;
    private Food food;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample user
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        // Create a sample food item
        food = new Food();
        food.setId(1L);
        food.setName("Apple");
        food.setQuantity(10);
        food.setPrice(2.99);
        food.setExpiryDate(LocalDate.now().plusDays(5));
        food.setStorageTemperature(4.0);
        food.setStorageLocation("Fridge");
        food.setNutritionInfo("Calories: 50, Protein: 1g");
        food.setUser(user);
    }

    @Test
    void shouldAddFoodForUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(foodRepository.save(food)).thenReturn(food);

        // When
        Food savedFood = foodService.addFood(1L, food);

        // Then
        assertNotNull(savedFood);
        assertEquals("Apple", savedFood.getName());
        assertEquals(user, savedFood.getUser());
        verify(foodRepository, times(1)).save(food);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnAddFood() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> foodService.addFood(1L, food));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldUpdateFoodForUser() {
        // Given
        Food updatedFood = new Food();
        updatedFood.setName("Orange");
        updatedFood.setQuantity(15);
        updatedFood.setPrice(3.49);
        updatedFood.setExpiryDate(LocalDate.now().plusDays(7));
        updatedFood.setStorageTemperature(5.0);
        updatedFood.setStorageLocation("Room Temperature");
        updatedFood.setNutritionInfo("Calories: 60, Protein: 1g");

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        when(foodRepository.save(food)).thenReturn(food);

        // When
        Food updatedFoodItem = foodService.updateFood(1L, 1L, updatedFood);

        // Then
        assertNotNull(updatedFoodItem);
        assertEquals("Orange", updatedFoodItem.getName());
        assertEquals(15, updatedFoodItem.getQuantity());
        assertEquals(3.49, updatedFoodItem.getPrice());
        verify(foodRepository, times(1)).save(food);
    }

    @Test
    void shouldThrowExceptionWhenFoodNotFoundOnUpdate() {
        // Given
        Food updatedFood = new Food();
        updatedFood.setName("Orange");

        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> foodService.updateFood(1L, 1L, updatedFood));
        assertEquals("Food not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFoodDoesNotBelongToUserOnUpdate() {
        // Given
        Food updatedFood = new Food();
        updatedFood.setName("Orange");

        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> foodService.updateFood(2L, 1L, updatedFood));
        assertEquals("Food does not belong to this user", exception.getMessage());
    }

    @Test
    void shouldDeleteFoodForUser() {
        // Given
        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));
        doNothing().when(foodRepository).delete(food);

        // When
        foodService.deleteFood(1L, 1L);

        // Then
        verify(foodRepository, times(1)).delete(food);
    }

    @Test
    void shouldThrowExceptionWhenFoodNotFoundOnDelete() {
        // Given
        when(foodRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> foodService.deleteFood(1L, 1L));
        assertEquals("Food not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFoodDoesNotBelongToUserOnDelete() {
        // Given
        when(foodRepository.findById(1L)).thenReturn(Optional.of(food));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> foodService.deleteFood(2L, 1L));
        assertEquals("Food does not belong to this user", exception.getMessage());
    }

    @Test
    void shouldGetAllFoodsForUser() {
        // Given
        when(foodRepository.findByUserId(1L)).thenReturn(List.of(food));

        // When
        List<Food> foods = foodService.getAllFoodsForUser(1L);

        // Then
        assertNotNull(foods);
        assertEquals(1, foods.size());
        assertEquals("Apple", foods.getFirst().getName());
    }

    @Test
    void shouldGetAllFoods() {
        // Given
        when(foodRepository.findAll()).thenReturn(List.of(food));

        // When
        List<Food> foods = foodService.getAllFoods();

        // Then
        assertNotNull(foods);
        assertEquals(1, foods.size());
        assertEquals("Apple", foods.getFirst().getName());
    }
}
