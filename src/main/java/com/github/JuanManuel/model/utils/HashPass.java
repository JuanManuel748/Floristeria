package com.github.JuanManuel.model.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPass {

    /**
     * Hashea la contraseña utilizando el algoritmo SHA-256.
     *
     * @param password La contraseña que se desea hashear.
     * @return La contraseña hasheada en formato hexadecimal.
     * @throws RuntimeException Si ocurre un error al utilizar el algoritmo SHA-256.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}