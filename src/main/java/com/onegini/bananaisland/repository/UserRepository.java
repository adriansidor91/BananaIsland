package com.onegini.bananaisland.repository;

import com.onegini.bananaisland.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 *
 * @author Adrian Sidor
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
