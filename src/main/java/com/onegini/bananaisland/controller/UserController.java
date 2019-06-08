package com.onegini.bananaisland.controller;

import com.onegini.bananaisland.exception.UserNotFoundException;
import com.onegini.bananaisland.model.Transaction;
import com.onegini.bananaisland.model.User;
import com.onegini.bananaisland.service.OneTimeTokenService;
import com.onegini.bananaisland.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller to perform banking operations.
 *
 * @author Adrian Sidor
 */
@RestController
public class UserController {
    private static final String TOKEN_KEY = "token";
    private static final String VALUE_KEY = "value";

    @Autowired
    private OneTimeTokenService tokenService;
    @Autowired
    private UserService userService;

    /**
     * Generates one time password to secure operations.
     *
     * @param userId
     * @return the response entity with generated token.
     */
    @PostMapping(value = "/tokens/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> generateToken(@PathVariable String userId) {
        JSONObject generatedTokenJson = new JSONObject();
        generatedTokenJson.put(TOKEN_KEY, tokenService.generateToken(userId));

        return new ResponseEntity<String>(generatedTokenJson.toString(), HttpStatus.CREATED);
    }

    /**
     * Performs balance decrease operation for the specified user.
     * The operation is secured using one time password.
     *
     * @param userId
     * @param requestBody the body contains decreasing value and token
     * @return the response entity with HTTP200 is token is valid, otherwise HTTP401 .
     */
    @PostMapping(value = "balance/user/{userId}/decrease", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> decreaseBalance(@PathVariable String userId, @RequestBody String requestBody) {
        JSONObject requestBodyJson = new JSONObject(requestBody);

        if(tokenService.isTokenValid(requestBodyJson.getString(TOKEN_KEY), userId)) {
            int decreasingValue = requestBodyJson.getInt(VALUE_KEY);
            userService.decreaseBalance(userId, decreasingValue);

            return new ResponseEntity<String>(HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Returns the current balance value for the specified user.
     *
     * @param userId
     * @return the response entity with user balance.
     */
    @GetMapping(value = "balance/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> getUserBalance(@PathVariable String userId){
        User user = userService.findById(userId);
        JSONObject balanceValueJson = new JSONObject();
        balanceValueJson.put(VALUE_KEY, user.getBalance());

        return new ResponseEntity<String>(balanceValueJson.toString(), HttpStatus.OK);
    }

    /**
     * Returns the history of the transactions from the first operation.
     *
     * @param userId
     * @return the response entity with user transaction history.
     */
    @GetMapping("history/user/{userId}")
    public ResponseEntity<List<Transaction>> showUserHistory(@PathVariable String userId) {
        User user = userService.findById(userId);

        return new ResponseEntity<List<Transaction>>(user.getTransactions(), HttpStatus.OK);
    }

    /**
     * Performs balance increase operation for the specified user.
     *
     * @param userId
     * @param requestBody the body contains increasing value
     * @return the response entity with HTTP200
     */
    @PostMapping(value = "balance/user/{userId}/increase", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> increaseBalance(@RequestBody String requestBody, @PathVariable String userId) {
        JSONObject requestBodyJson = new JSONObject(requestBody);
        int increasingValue = requestBodyJson.getInt(VALUE_KEY);
        userService.increaseBalance(userId, increasingValue);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
