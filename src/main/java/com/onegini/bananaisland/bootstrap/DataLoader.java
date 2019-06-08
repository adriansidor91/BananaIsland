package com.onegini.bananaisland.bootstrap;

import com.onegini.bananaisland.model.User;
import com.onegini.bananaisland.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * DataLoader creates user and saves it in DB every time when application is starting.
 *
 * @author Adrian Sidor
 */
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        User user = new User();
        user.setId(1L);
        user.setBalance(1000);
        userRepository.save(user);
    }
}
