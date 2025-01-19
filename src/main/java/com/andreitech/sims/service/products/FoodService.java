package com.andreitech.sims.service.products;

import com.andreitech.sims.entity.User;
import com.andreitech.sims.entity.products.Food;
import com.andreitech.sims.repository.UserRepository;
import com.andreitech.sims.repository.products.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;

    // Dependency Injection
    @Autowired
    public FoodService(FoodRepository foodRepository, UserRepository userRepository) {
        this.foodRepository = foodRepository;
        this.userRepository = userRepository;
    }

    // Add a food item for a user
    public Food addFood(Long userId, Food food) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            food.setUser(user.get());
            food.setCategory("Food");
            return foodRepository.save(food);
        }
        throw new RuntimeException("User not found");
    }

    // Update a food item for a user
    public Food updateFood(Long userId, Long foodId, Food updatedFood) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        if (foodOpt.isPresent()) {
            Food food = foodOpt.get();
            if (food.getUser().getId().equals(userId)) {
                food.setName(updatedFood.getName());
                food.setQuantity(updatedFood.getQuantity());
                food.setPrice(updatedFood.getPrice());
                food.setExpiryDate(updatedFood.getExpiryDate());
                food.setStorageTemperature(updatedFood.getStorageTemperature());
                food.setStorageLocation(updatedFood.getStorageLocation());
                food.setNutritionInfo(updatedFood.getNutritionInfo());
                return foodRepository.save(food);
            } else {
                throw new RuntimeException("Food does not belong to this user");
            }
        }
        throw new RuntimeException("Food not found");
    }

    // Delete a food item for a user
    public void deleteFood(Long userId, Long foodId) {
        Optional<Food> foodOpt = foodRepository.findById(foodId);
        if (foodOpt.isPresent()) {
            Food food = foodOpt.get();
            if (food.getUser().getId().equals(userId)) {
                foodRepository.delete(food);
            } else {
                throw new RuntimeException("Food does not belong to this user");
            }
        } else {
            throw new RuntimeException("Food not found");
        }
    }

    // Get all food items for a user
    public List<Food> getAllFoodsForUser(Long userId) {
        return foodRepository.findByUserId(userId);
    }

    // Get all food items
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
}
