package com.github.JuanManuel.test;

import com.github.JuanManuel.model.utils.HashPass;

public class TestHashedPassword {

    public static void main(String[] args) {

        String contraseña = String.valueOf(1);
        String hashed = HashPass.hashPassword(contraseña);
        System.out.println(hashed);
    }
}
