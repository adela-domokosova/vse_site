package com.example.vse_site;

public interface PasswordEncoder {
    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encoderPassword);

    default boolean upgradeEncoding(String encodedPassword){
        return false;
    }
}
