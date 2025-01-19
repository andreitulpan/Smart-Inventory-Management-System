package com.andreitech.sims.entity.products;

import com.andreitech.sims.entity.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "foods")
@Getter
@Setter
public class Food extends Product {

    private LocalDate expiryDate;

    private Double storageTemperature;

    private String storageLocation;

    private String nutritionInfo;

    @JsonIgnore
    @Transient
    private long daysUntilExpiry;

    public void calculateDaysUntilExpiry() {
        daysUntilExpiry = LocalDate.now().until(expiryDate, java.time.temporal.ChronoUnit.DAYS);
    }
}

