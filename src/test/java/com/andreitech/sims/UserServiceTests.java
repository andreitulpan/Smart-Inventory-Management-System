package com.andreitech.sims;

import com.andreitech.sims.entity.User;
import com.andreitech.sims.repository.UserRepository;
import com.andreitech.sims.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes the mock objects
        userService = new UserService(userRepository);
    }

    @Test
    void testAddUser() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        when(userRepository.save(user)).thenReturn(user);

        // When
        User result = userService.addUser(user);

        // Then
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAllUsers() {
        // Given
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // When
        List<User> users = userService.getAllUsers();

        // Then
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user2", users.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        User result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(1L);
        });
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void testUpdateUser() {
        // Given
        User existingUser = new User();
        existingUser.setUsername("oldUser");
        existingUser.setEmail("old@example.com");
        User updatedUser = new User();
        updatedUser.setUsername("newUser");
        updatedUser.setEmail("new@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        // When
        User result = userService.updateUser(1L, updatedUser);

        // Then
        assertEquals("newUser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testDeleteUser() {
        // Given
        Long userId = 1L;

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).deleteById(userId);
    }
}
