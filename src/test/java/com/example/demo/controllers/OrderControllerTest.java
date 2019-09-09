package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    private OrderController orderController;

    @Mock
    UserRepository userRepository;

    @Mock
    OrderRepository orderRepository;

    @Before
    public void init(){

        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submitOrderByInvalidUsername() throws Exception {

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<UserOrder> responseEntity = orderController.submit("Ron");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void submitOrderByValidUsername() throws Exception {

        Cart mockCart = Mockito.mock(Cart.class);
        mockCart.setItems(new ArrayList<>());

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<UserOrder> responseEntity = orderController.submit("Draco");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void getOrdersForInvalidUser() throws Exception {

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("Ron");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getOrdersForValidUser() throws Exception {

        Cart mockCart = Mockito.mock(Cart.class);
        mockCart.setItems(new ArrayList<>());

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser( "Draco");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


}