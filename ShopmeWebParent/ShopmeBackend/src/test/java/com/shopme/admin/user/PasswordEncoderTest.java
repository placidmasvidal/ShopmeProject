package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTest {

    @Test
    public void testEncodePassword(){
        BCryptPasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder();
        String rawPassword = "pmf2021";
        String encodedPassword = passwordEncoder.encode(rawPassword);

    System.out.println(encodedPassword);

    assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}
