package com.andreitech.sims.service.products;

import com.andreitech.sims.entity.User;
import com.andreitech.sims.entity.products.Medication;
import com.andreitech.sims.repository.UserRepository;
import com.andreitech.sims.repository.products.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;

    // Dependency Injection
    @Autowired
    public MedicationService(MedicationRepository medicationRepository, UserRepository userRepository) {
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
    }

    // Add a medication for a user
    public Medication addMedication(Long userId, Medication medication) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            medication.setUser(user.get());
            medication.setCategory("Medication");
            return medicationRepository.save(medication);
        }
        throw new RuntimeException("User not found");
    }

    // Update a medication for a user
    public Medication updateMedication(Long userId, Long medicationId, Medication updatedMedication) {
        Optional<Medication> medicationOpt = medicationRepository.findById(medicationId);
        if (medicationOpt.isPresent()) {
            Medication medication = medicationOpt.get();
            if (medication.getUser().getId().equals(userId)) {
                medication.setName(updatedMedication.getName());
                medication.setQuantity(updatedMedication.getQuantity());
                medication.setPrice(updatedMedication.getPrice());
                medication.setExpiryDate(updatedMedication.getExpiryDate());
                medication.setDosageInstructions(updatedMedication.getDosageInstructions());
                medication.setRequiresPrescription(updatedMedication.getRequiresPrescription());
                return medicationRepository.save(medication);
            } else {
                throw new RuntimeException("Medication does not belong to this user");
            }
        }
        throw new RuntimeException("Medication not found");
    }

    // Delete a medication for a user
    public void deleteMedication(Long userId, Long medicationId) {
        Optional<Medication> medicationOpt = medicationRepository.findById(medicationId);
        if (medicationOpt.isPresent()) {
            Medication medication = medicationOpt.get();
            if (medication.getUser().getId().equals(userId)) {
                medicationRepository.delete(medication);
            } else {
                throw new RuntimeException("Medication does not belong to this user");
            }
        } else {
            throw new RuntimeException("Medication not found");
        }
    }

    // Get all medications for a user
    public List<Medication> getAllMedicationsForUser(Long userId) {
        return medicationRepository.findByUserId(userId);
    }

    // Get all medications
    public List<Medication> getAllMedications() {
        return medicationRepository.findAll();
    }
}
