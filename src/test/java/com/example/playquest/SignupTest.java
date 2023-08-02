package com.example.playquest;

import com.example.playquest.controllers.Signup;
import com.example.playquest.entities.User;
import com.example.playquest.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SignupTest {
    @InjectMocks
    Signup signupController;

    @Mock
    UsersRepository usersRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSignupForm() {
        String result = signupController.signupForm();
        assertEquals("signup", result);
    }

    @Test
    public void testSignup() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("testpass");

        when(usersRepository.save(any(User.class))).thenReturn(user);

        String result = signupController.signup(user);
        assertEquals("redirect:/login", result);

        verify(usersRepository, times(1)).save(any(User.class));
    }

}
