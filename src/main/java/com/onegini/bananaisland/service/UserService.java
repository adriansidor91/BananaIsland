package com.onegini.bananaisland.service;

import com.onegini.bananaisland.exception.NotEnoughMoneyException;
import com.onegini.bananaisland.exception.UserNotFoundException;
import com.onegini.bananaisland.model.Transaction;
import com.onegini.bananaisland.model.TransactionType;
import com.onegini.bananaisland.model.User;
import com.onegini.bananaisland.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service finds user and performs bank account transactions.
 *
 * @author Adrian Sidor
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionService transactionService;

    /**
     * Retrieves a user by its id.
     *
     * @param userId
     * @return the user with given id
     * @throws UserNotFoundException if user not found.
     */
    public User findById(String userId) {
        Optional<User> user = userRepository.findById(Long.parseLong(userId));

        if(!user.isPresent()) {
            throw new UserNotFoundException("Affected user id: " + userId.toString());
        }

        return user.get();
    }

    /**
     * Increases user balance and registers new money transaction.
     *
     * @param userId
     * @param increasingValue
     */
    @Transactional
    public void increaseBalance(String userId, int increasingValue) {
        User user = findById(userId);

        int newBalance = user.getBalance() + increasingValue;
        user.setBalance(newBalance);
        Transaction transaction = transactionService.createTransaction(TransactionType.INCREASE, increasingValue);
        user.addTransaction(transaction);
    }

    /**
     * Decreases user balance and registers new money transaction.
     *
     * @param userId
     * @param decreasingValue
     * @throws NotEnoughMoneyException if user does not have enough money to perform this operation
     */
    @Transactional
    public void decreaseBalance(String userId, int decreasingValue) {
        User user = findById(userId);
        int balance = user.getBalance();

        if(balance > decreasingValue) {
            user.setBalance(balance - decreasingValue);
            Transaction transaction = transactionService.createTransaction(TransactionType.DECREASE, decreasingValue);
            user.addTransaction(transaction);
        } else {
            throw new NotEnoughMoneyException("You do not have enough money.");
        }
    }
}
