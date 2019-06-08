package com.onegini.bananaisland.service;

import com.onegini.bananaisland.exception.NotEnoughMoneyException;
import com.onegini.bananaisland.exception.UserNotFoundException;
import com.onegini.bananaisland.model.Transaction;
import com.onegini.bananaisland.model.TransactionType;
import com.onegini.bananaisland.model.User;
import com.onegini.bananaisland.repository.UserRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.onegini.bananaisland.model.TransactionType.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    private static  final Long USER_ID = 1L;
    private static final int BALANCE = 1000;

    @Mock
    private UserRepository userRepository;
    @Mock
    private TransactionService transactionService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private UserService userService = new UserService();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(createUser()));

        //when
        User user = userService.findById(USER_ID.toString());
        //then
        assertEquals(USER_ID, user.getId());
    }

    @Test
    public void testFindByIdThrowsUserNotFoundException() {
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.empty());
        expectedException.expect(UserNotFoundException.class);
        expectedException.expectMessage("Affected user id: " + USER_ID.toString());

        userService.findById(USER_ID.toString());
    }

    @Test
    public void testIncreaseBalance() {
        //given
        int increasingValue = 500;
        User user = createUser();
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(user));
        when(transactionService.createTransaction(eq(INCREASE), eq(increasingValue))).thenReturn(createTransaction(increasingValue, INCREASE));
        //when
        userService.increaseBalance(USER_ID.toString(), increasingValue);
        //then
        assertEquals(BALANCE + increasingValue, user.getBalance());
        assertEquals(1,user.getTransactions().size());
        assertEquals(increasingValue, user.getTransactions().get(0).getValue());
    }

    @Test
    public void testDecreaseBalance() {
        //given
        int decreasingValue = 200;
        User user = createUser();
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(user));
        when(transactionService.createTransaction(eq(DECREASE), eq(decreasingValue))).thenReturn(createTransaction(decreasingValue, DECREASE));
        //when
        userService.decreaseBalance(USER_ID.toString(), decreasingValue);
        //then
        assertEquals(BALANCE - decreasingValue, user.getBalance());
        assertEquals(1, user.getTransactions().size());
        assertEquals(decreasingValue, user.getTransactions().get(0).getValue());
    }

    @Test
    public void testDecreasingValeThrowsNotEnoughMoneyException() {
        expectedException.expect(NotEnoughMoneyException.class);

        int decreasingValue = 1200;
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(createUser()));
        userService.decreaseBalance(USER_ID.toString(), decreasingValue);
    }

    private Transaction createTransaction(int value, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setValue(value);
        transaction.setTransactionType(transactionType);

        return transaction;
    }

    private User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setBalance(BALANCE);

        return user;
    }
}
