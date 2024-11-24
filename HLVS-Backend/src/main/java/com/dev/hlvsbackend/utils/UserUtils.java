package com.dev.hlvsbackend.utils;

public class UserUtils {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }

        public UserNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        public UserNotFoundException() {
            super("User not found");
        }
    }


}
