package com.andreitech.sims.products;

import com.andreitech.sims.entity.User;
import com.andreitech.sims.entity.products.Medication;
import com.andreitech.sims.repository.UserRepository;
import com.andreitech.sims.repository.products.MedicationRepository;
import com.andreitech.sims.service.products.MedicationService;
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

class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MedicationService medicationService;

    private User user;
    private Medication medication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample user
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        // Create a sample medication item
        medication = new Medication();
        medication.setId(1L);
        medication.setName("Ibuprofen");
        medication.setQuantity(30);
        medication.setPrice(5.99);
        medication.setExpiryDate(LocalDate.now().plusDays(365));
        medication.setDosageInstructions("Take one pill every 6 hours");
        medication.setRequiresPrescription(true);
        medication.setUser(user);
    }

    @Test
    void shouldAddMedicationForUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(medicationRepository.save(medication)).thenReturn(medication);

        // When
        Medication savedMedication = medicationService.addMedication(1L, medication);

        // Then
        assertNotNull(savedMedication);
        assertEquals("Ibuprofen", savedMedication.getName());
        assertEquals(user, savedMedication.getUser());
        verify(medicationRepository, times(1)).save(medication);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundOnAddMedication() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicationService.addMedication(1L, medication));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void shouldUpdateMedicationForUser() {
        // Given
        Medication updatedMedication = new Medication();
        updatedMedication.setName("Paracetamol");
        updatedMedication.setQuantity(50);
        updatedMedication.setPrice(3.99);
        updatedMedication.setExpiryDate(LocalDate.now().plusDays(180));
        updatedMedication.setDosageInstructions("Take two pills every 8 hours");
        updatedMedication.setRequiresPrescription(false);

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(medication)).thenReturn(medication);

        // When
        Medication updatedMedicationItem = medicationService.updateMedication(1L, 1L, updatedMedication);

        // Then
        assertNotNull(updatedMedicationItem);
        assertEquals("Paracetamol", updatedMedicationItem.getName());
        assertEquals(50, updatedMedicationItem.getQuantity());
        assertEquals(3.99, updatedMedicationItem.getPrice());
        verify(medicationRepository, times(1)).save(medication);
    }

    @Test
    void shouldThrowExceptionWhenMedicationNotFoundOnUpdate() {
        // Given
        Medication updatedMedication = new Medication();
        updatedMedication.setName("Paracetamol");

        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicationService.updateMedication(1L, 1L, updatedMedication));
        assertEquals("Medication not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMedicationDoesNotBelongToUserOnUpdate() {
        // Given
        Medication updatedMedication = new Medication();
        updatedMedication.setName("Paracetamol");

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicationService.updateMedication(2L, 1L, updatedMedication));
        assertEquals("Medication does not belong to this user", exception.getMessage());
    }

    @Test
    void shouldDeleteMedicationForUser() {
        // Given
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        doNothing().when(medicationRepository).delete(medication);

        // When
        medicationService.deleteMedication(1L, 1L);

        // Then
        verify(medicationRepository, times(1)).delete(medication);
    }

    @Test
    void shouldThrowExceptionWhenMedicationNotFoundOnDelete() {
        // Given
        when(medicationRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicationService.deleteMedication(1L, 1L));
        assertEquals("Medication not found", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenMedicationDoesNotBelongToUserOnDelete() {
        // Given
        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> medicationService.deleteMedication(2L, 1L));
        assertEquals("Medication does not belong to this user", exception.getMessage());
    }

    @Test
    void shouldGetAllMedicationsForUser() {
        // Given
        when(medicationRepository.findByUserId(1L)).thenReturn(List.of(medication));

        // When
        List<Medication> medications = medicationService.getAllMedicationsForUser(1L);

        // Then
        assertNotNull(medications);
        assertEquals(1, medications.size());
        assertEquals("Ibuprofen", medications.get(0).getName());
    }

    @Test
    void shouldGetAllMedications() {
        // Given
        when(medicationRepository.findAll()).thenReturn(List.of(medication));

        // When
        List<Medication> medications = medicationService.getAllMedications();

        // Then
        assertNotNull(medications);
        assertEquals(1, medications.size());
        assertEquals("Ibuprofen", medications.get(0).getName());
    }
}
