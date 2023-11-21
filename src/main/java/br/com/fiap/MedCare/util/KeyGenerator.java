package br.com.fiap.MedCare.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[32]; 
        secureRandom.nextBytes(bytes);
        String key = Base64.getEncoder().encodeToString(bytes);
        System.out.println("Nova Chave Secreta: " + key);
    }
}
