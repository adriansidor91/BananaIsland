package com.onegini.bananaisland.service;

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.onegini.bananaisland.model.User;
import com.onegini.bananaisland.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Service generates one time password and keeps it in guava cache.
 * Password is expired after 5 minutes if it was not used.
 *
 * @author Adrian Sidor
 */
@Service
public class OneTimeTokenService {
    private static final Integer EXPIRE_MINS = 5;

    private LoadingCache<String, String> tokenCache;

    @Autowired
    private UserRepository userRepository;

    public OneTimeTokenService(){
        tokenCache = CacheBuilder.newBuilder()
                    .expireAfterWrite(EXPIRE_MINS, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
                        public String load(String key) {
                            return "";
                        }
        });
    }

    /**
     * Generates one time token and puts it into cache.
     *
     * @param userId the key for token cache
     * @return the user token
     * @throws IllegalArgumentException if user not exist.
     */
    public String generateToken(String userId){
        Optional<User> user = userRepository.findById(Long.parseLong(userId));

        if (user.isPresent()) {
            Random random = new Random();
            Integer token = 100000 + random.nextInt(900000);
            tokenCache.put(userId, token.toString());
            return token.toString();
        } else {
         throw new IllegalArgumentException("You cannot generate token for not existing user. Affected id: " + userId);
        }
    }

    /**
     * Compares received token with token from cache .
     *
     * @param receivedToken token to be validated
     * @param userId the key for token cache
     * @return the true if token is valid, otherwise false
     */
    public boolean isTokenValid(String receivedToken, String userId) {
        if (!Strings.isNullOrEmpty(receivedToken)) {
            String cachedToken = getToken(userId);

            if (cachedToken.equals(receivedToken)) {
                clearToken(userId);
                return true;
            }
        }
        return false;
    }

    private String getToken(String userId){
        try{
            return tokenCache.get(userId);
        }catch (Exception e){
            return "";
        }
    }

    private void clearToken(String key){
        tokenCache.invalidate(key);
    }

}
