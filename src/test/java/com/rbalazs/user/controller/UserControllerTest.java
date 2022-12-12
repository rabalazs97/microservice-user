package com.rbalazs.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbalazs.user.dto.FibonacciResponseDTO;
import com.rbalazs.user.dto.FibonacciSaveRequestDTO;
import com.rbalazs.user.dto.UserRegistrationDTO;
import com.rbalazs.user.service.FibonacciService;
import com.rbalazs.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigInteger;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@WebMvcTest
class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private FibonacciService fibonacciService;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldBeAbleToGetHardcodedTextMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Hello buddy!"));
    }

    @Test
    void shouldBeAbleToAddUserToDatabase() throws Exception {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("Test");
        mockMvc.perform(MockMvcRequestBuilders.post("/").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userRegistrationDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User named " + userRegistrationDTO.getName() + " has been added to the database."));
    }

    @Test
    void shouldBeAbleToGetUserIdByTheUserName() throws Exception {
        String userName = "Test";
        int userId = 1;
        given(userService.getUserIdByName(userName)).willReturn(userId);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/"+userName))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(String.valueOf(userId)));
    }

    @Test
    void shouldGetCalculationHistory() throws Exception {
        FibonacciResponseDTO fibonacciResponseDTO = new FibonacciResponseDTO(10, new BigInteger("55"));
        FibonacciResponseDTO fibonacciResponseDTO2 = new FibonacciResponseDTO(11, new BigInteger("89"));
        given(fibonacciService.getHistory()).willReturn(Arrays.asList(fibonacciResponseDTO,fibonacciResponseDTO2));
        mockMvc.perform(MockMvcRequestBuilders.get("/history"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(Arrays.asList(fibonacciResponseDTO,fibonacciResponseDTO2))));
    }

    @Test
    void shouldGetOnlyUserSpecificCalculationHistory() throws Exception {
        int userId = 1;
        FibonacciResponseDTO fibonacciResponseDTO = new FibonacciResponseDTO(10, new BigInteger("55"));
        FibonacciResponseDTO fibonacciResponseDTO2 = new FibonacciResponseDTO(11, new BigInteger("89"));
        given(fibonacciService.getUserSpecificHistory(userId)).willReturn(Arrays.asList(fibonacciResponseDTO,fibonacciResponseDTO2));
        mockMvc.perform(MockMvcRequestBuilders.get("/history/" + userId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(objectMapper.writeValueAsString(Arrays.asList(fibonacciResponseDTO,fibonacciResponseDTO2))));
    }

    @Test
    void shouldBeAbleToAddCalculationResultToDatabase() throws Exception {
        int serialNumber = 10;
        byte[] fibNumber = new BigInteger("55").toByteArray();
        int userId = 1;
        FibonacciSaveRequestDTO fibonacciSaveRequestDTO = new FibonacciSaveRequestDTO(serialNumber, fibNumber, userId);
        mockMvc.perform(MockMvcRequestBuilders.post("/save").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(fibonacciSaveRequestDTO)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}