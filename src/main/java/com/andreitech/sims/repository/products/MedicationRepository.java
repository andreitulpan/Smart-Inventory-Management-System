package com.andreitech.sims.repository.products;

import com.andreitech.sims.entity.products.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long> {

    // Find all medications for a specific user
    List<Medication> findByUserId(Long userId);
}
