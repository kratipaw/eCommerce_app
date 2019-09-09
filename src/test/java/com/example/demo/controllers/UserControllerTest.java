package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private CreateUserRequest getMockUserForCreateUserRequest(String userName, String pass, String confirmPass){
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername(userName);
        user.setPassword(pass);
        user.setConfirmPassword(confirmPass);
        return user;
    }

    @Before
    public void init() throws Exception {

        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", this.userRepository);
        TestUtils.injectObjects(userController, "cartRepository", this.cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", this.bCryptPasswordEncoder);
    }

    @Test
    public void createUserWithShortPassword() throws Exception {
        CreateUserRequest createUser = getMockUserForCreateUserRequest("Sachin", "pass", "pass");

        ResponseEntity<User> response = userController.createUser(createUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    public void createUserWithMismatchedPasswords() throws Exception {
        CreateUserRequest createUser = getMockUserForCreateUserRequest("Sachin", "password", "pass");

        ResponseEntity<User> response = userController.createUser(createUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createValidUser() {
        CreateUserRequest createUser = getMockUserForCreateUserRequest("Harry", "password", "password");

        ResponseEntity<User> response = userController.createUser(createUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void findByUserNameWithAuth() throws Exception {

        User mockUser = new User();
        mockUser.setUsername("Harry");

        when(this.userRepository.findByUsername("Harry")).thenReturn(mockUser);

        ResponseEntity<User> responseEntity = userController.findByUserName("Harry");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void findInvalidUserNameWithAuth() throws Exception {

        User mockUser = new User();
        mockUser.setUsername("Harry");

        when(this.userRepository.findByUsername("Harry")).thenReturn(mockUser);

        ResponseEntity<User> responseEntity = userController.findByUserName("Ron");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void findByIdWithAuth() throws Exception {

        User mockUser = new User();
        mockUser.setUsername("Harry");

        when(this.userRepository.findById(1L)).thenReturn(java.util.Optional.of(mockUser));

        ResponseEntity<User> responseEntity = userController.findById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void findInvalidIdWithAuth() throws Exception {

        when(this.userRepository.findAllById(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<User> responseEntity = userController.findById(5L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}