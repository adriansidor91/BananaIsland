package com.onegini.bananaisland.service;

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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class OneTimeTokenServiceTest {

    private static final Long USER_ID = 1L;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OneTimeTokenService oneTimeTokenService = new OneTimeTokenService();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGenerateToken() {
        //given
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(createUser()));
        //when
        String token = oneTimeTokenService.generateToken(USER_ID.toString());
        boolean isTokenValid = oneTimeTokenService.isTokenValid(token, USER_ID.toString());
        boolean isTokenStillValid = oneTimeTokenService.isTokenValid(token, USER_ID.toString());
        //then
        assertTrue(isTokenValid);
        assertFalse(isTokenStillValid);
    }

    @Test
    public void testGenerateTokenForNotExistingUser() {
        expectedException.expect(IllegalArgumentException.class);

        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.empty());

        oneTimeTokenService.generateToken(USER_ID.toString());
    }

    @Test
    public void testIsTokenValid() {
        //given
        when(userRepository.findById(eq(USER_ID))).thenReturn(Optional.of(createUser()));
        //when
        String validToken = oneTimeTokenService.generateToken(USER_ID.toString());
        String notValidToken = validToken + "1";
        boolean isTokenValid = oneTimeTokenService.isTokenValid(notValidToken, USER_ID.toString());
        //then
        assertFalse(isTokenValid);
    }

    private User createUser() {
        User user = new User();
        user.setId(USER_ID);

        return user;
    }
}
