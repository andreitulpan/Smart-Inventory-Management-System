package com.andreitech.sims.repository.products;

import com.andreitech.sims.entity.products.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    // Find all foods for a specific user
    List<Food> findByUserId(Long userId);
}
