package com.onegini.bananaisland.service;

import com.onegini.bananaisland.model.Transaction;
import com.onegini.bananaisland.model.TransactionType;
import com.onegini.bananaisland.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService = new TransactionService();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTransaction() {
        //when
        Transaction savedTransaction = transactionService.createTransaction(TransactionType.DECREASE, 100);
        //then
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
