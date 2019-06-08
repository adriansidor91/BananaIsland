package com.onegini.bananaisland.service;

import com.onegini.bananaisland.model.Transaction;
import com.onegini.bananaisland.model.TransactionType;
import com.onegini.bananaisland.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service creates and saves new money transaction.
 *
 * @author Adrian Sidor
 */
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(TransactionType transactionType, int value) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionType);
        transaction.setValue(value);

        return transactionRepository.save(transaction);
    }
}
