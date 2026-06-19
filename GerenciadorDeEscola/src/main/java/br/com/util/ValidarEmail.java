package br.com.util;

public class ValidarEmail {
    public static boolean validarEmail(String email){
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}