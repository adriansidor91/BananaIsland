package com.onegini.bananaisland.controller;

import com.onegini.bananaisland.model.Transaction;
import com.onegini.bananaisland.model.TransactionType;
import com.onegini.bananaisland.model.User;
import com.onegini.bananaisland.service.OneTimeTokenService;
import com.onegini.bananaisland.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UserControllerTest {

    private static final Long USER_ID = 1L;
    private static final int BALANCE = 1000;
    private static final int TRANSACTION_VALUE = 100;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OneTimeTokenService tokenService;
    @MockBean
    private UserService userService;

    @Test
    public void testGetUserBalance() throws Exception {
        //given
        User user = createUser();
        given(userService.findById(USER_ID.toString())).willReturn(user);
        //when + then
        this.mockMvc.perform(
                get("/balance/user/{userId}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json("{'value':1000}"));
    }

    @Test
    public void testIncreaseBalance() throws Exception {
        //given
        String requestBody = "{'value':100}";
        //when + then
        this.mockMvc.perform(
                post("/balance/user/{userId}/increase", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testDecreaseBalance() throws Exception {
        //given
        String requestBody = "{'value':100, 'token':'12345'}";
        given(tokenService.isTokenValid(anyString(),eq(USER_ID.toString()))).willReturn(true);

        //when + then
        this.mockMvc.perform(
                post("/balance/user/{userId}/decrease", USER_ID)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetHistory() throws Exception {
        //given
        User user = createUser();
        user.addTransaction(createTransaction());
        given(userService.findById(USER_ID.toString())).willReturn(user);

        String expectedJsonHistory = "[{'type':'increase','value':100}]";

        //when+then
        this.mockMvc.perform(
                get("/history/user/{userId}", USER_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(expectedJsonHistory));
    }

    @Test
    public void testGenerateToken() throws Exception {
        //given
        given(tokenService.generateToken(USER_ID.toString())).willReturn("12345");

        //when+then
        this.mockMvc.perform(
                post("/tokens/user/{userId}", USER_ID))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json("{'token':'12345'}"));
    }

    private User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setBalance(BALANCE);

        return  user;
    }

    private Transaction createTransaction() {
        Transaction transaction = new Transaction();
        transaction.setValue(TRANSACTION_VALUE);
        transaction.setTransactionType(TransactionType.INCREASE);

        return transaction;
    }
}
