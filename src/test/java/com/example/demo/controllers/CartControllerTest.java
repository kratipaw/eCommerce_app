package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerTest {

    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    private ModifyCartRequest getCartRequest(String userName, Long itemId, int quantity) {

        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername(userName);
        cartRequest.setItemId(itemId);
        cartRequest.setQuantity(quantity);
        return cartRequest;
    }


    @Before
    public void init(){

        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

    }


    @Test
    public void addToCart() throws Exception {

        ModifyCartRequest cartRequest = getCartRequest("Harry", 1L, 9);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("Toy");
        mockItem.setPrice(new BigDecimal(10));

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(mockItem));

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void addToCartInvalidUsername() throws Exception {
        ModifyCartRequest cartRequest = getCartRequest("Sachin", 2L, 9);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void addToCartInvalidItemId() throws Exception {
        ModifyCartRequest cartRequestWithInvalidItemId = getCartRequest("Harry", 5L, 9);

        User mockUser = new User();
        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        ResponseEntity<Cart> responseEntity = cartController.addTocart(cartRequestWithInvalidItemId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void removeFromCart() throws Exception {

        ModifyCartRequest cartRequest = getCartRequest("Harry", 1L, 9);

        Cart mockCart = Mockito.mock(Cart.class);

        Item mockItem = new Item();
        mockItem.setName("Toy");
        mockItem.setPrice(new BigDecimal(10));

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.of(mockItem));

        User mockUser = new User();
        mockUser.setCart(mockCart);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void removeFromCartInvalidUsername() throws Exception {

        ModifyCartRequest cartRequest = getCartRequest("Sachin", 2L, 9);

        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(null);

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void removeFromCartInvalidItemId() throws Exception {

        ModifyCartRequest cartRequestWithInvalidItemId = getCartRequest("Harry", 5L, 9);

        User mockUser = new User();
        when(userRepository.findByUsername(ArgumentMatchers.any())).thenReturn(mockUser);

        when(itemRepository.findById(ArgumentMatchers.any())).thenReturn(Optional.empty());

        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(cartRequestWithInvalidItemId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}