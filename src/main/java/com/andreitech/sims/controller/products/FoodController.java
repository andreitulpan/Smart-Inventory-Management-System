package com.andreitech.sims.controller.products;

import com.andreitech.sims.entity.products.Food;
import com.andreitech.sims.service.products.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/foods")
public class FoodController {

    private final FoodService foodService;

    // Dependency Injection
    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    // Add a food item for a user
    @PostMapping
    public ResponseEntity<Food> addFood(@PathVariable Long userId, @RequestBody Food food) {
        Food createdFood = foodService.addFood(userId, food);
        return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
    }

    // Update a food item for a user
    @PutMapping("/{foodId}")
    public ResponseEntity<Food> updateFood(@PathVariable Long userId, @PathVariable Long foodId, @RequestBody Food updatedFood) {
        Food updated = foodService.updateFood(userId, foodId, updatedFood);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Delete a food item for a user
    @DeleteMapping("/{foodId}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long userId, @PathVariable Long foodId) {
        foodService.deleteFood(userId, foodId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all food items for a user
    @GetMapping
    public ResponseEntity<List<Food>> getAllFoodsForUser(@PathVariable Long userId) {
        List<Food> foods = foodService.getAllFoodsForUser(userId);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
