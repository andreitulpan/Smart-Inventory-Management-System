package com.andreitech.sims.controller.products;

import com.andreitech.sims.entity.products.Medication;
import com.andreitech.sims.service.products.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/medications")
public class MedicationController {

    private final MedicationService medicationService;

    // Dependency Injection
    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // Add a medication item for a user
    @PostMapping
    public ResponseEntity<Medication> addMedication(@PathVariable Long userId, @RequestBody Medication medication) {
        Medication createdMedication = medicationService.addMedication(userId, medication);
        return new ResponseEntity<>(createdMedication, HttpStatus.CREATED);
    }

    // Update a medication item for a user
    @PutMapping("/{medicationId}")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long userId, @PathVariable Long medicationId, @RequestBody Medication updatedMedication) {
        Medication updated = medicationService.updateMedication(userId, medicationId, updatedMedication);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Delete a medication item for a user
    @DeleteMapping("/{medicationId}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long userId, @PathVariable Long medicationId) {
        medicationService.deleteMedication(userId, medicationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get all medications for a user
    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedicationsForUser(@PathVariable Long userId) {
        List<Medication> medications = medicationService.getAllMedicationsForUser(userId);
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }
}
