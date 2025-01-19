package com.andreitech.sims.entity.products;

import com.andreitech.sims.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Getter
@Setter
public class Medication extends Product {

    private LocalDate expiryDate;

    private String dosageInstructions;

    private Boolean requiresPrescription;

    @Transient
    private long daysUntilExpiry;

    public void calculateDaysUntilExpiry() {
        daysUntilExpiry = LocalDate.now().until(expiryDate, java.time.temporal.ChronoUnit.DAYS);
    }
}
