package com.onegini.bananaisland.repository;

import com.onegini.bananaisland.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * TransactionRepository
 *
 * @author Adrian Sidor
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
