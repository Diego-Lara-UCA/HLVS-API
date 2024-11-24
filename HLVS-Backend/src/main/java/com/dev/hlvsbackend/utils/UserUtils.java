package com.dev.hlvsbackend.utils;

public class UserUtils {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }

        public UserNotFoundException() {
            super("User not found");
        }
    }


}
