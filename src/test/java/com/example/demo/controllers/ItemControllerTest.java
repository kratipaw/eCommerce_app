package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getAllItems() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/item")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonStr = result.getResponse().getContentAsString();

        List<Item> actualItemList = new ObjectMapper().readValue(jsonStr, new TypeReference<List<Item>>(){});

        assertNotNull(actualItemList);

        assertEquals(2, actualItemList.size());

        assertEquals("Round Widget", actualItemList.get(0).getName());

    }

    @Test
    public void getItemByValidId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/item/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonStr = result.getResponse().getContentAsString();

        Item item = new ObjectMapper().readValue(jsonStr, Item.class);

        assertNotNull(item);

        assertEquals("Round Widget", item.getName());

    }

    @Test
    public void getItemByInvalidId() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/item/{id}", 5)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getItemByValidItemName() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/item/name/{name}", "Round Widget")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonStr = result.getResponse().getContentAsString();

        List<Item> actualItemList = new ObjectMapper().readValue(jsonStr, new TypeReference<List<Item>>(){});

        assertNotNull(actualItemList);

        assertEquals(1, actualItemList.size());

        assertEquals("A widget that is round", actualItemList.get(0).getDescription());

    }

    @Test
    public void getItemByInvalidItem() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/item/name/{name}", "zebra")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

}