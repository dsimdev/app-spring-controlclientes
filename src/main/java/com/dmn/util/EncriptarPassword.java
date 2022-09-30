package com.dmn.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncriptarPassword {
    public static void main(String[] args) {
        
        String password = "123";
        System.out.println("password: " + password);
        System.out.println("password encriptado:" + encryptPassword(password));
    }
    
    public static String encryptPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
