package br.com.util;

import br.com.model.entity.Pessoa;

import java.util.List;

public class ValidarCpf {
    public static boolean validarCpf(String cpf){
        cpf = cpf.replaceAll("\\D", "");

        if(cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        try{
            int soma = 0;

            for(int i = 0; i<9; i++){
                soma += (cpf.charAt(i) - '0') * (10 -i);
            }

            int resto = 11 - (soma%11);
            int digito1 = (resto >= 10) ? 0 : resto;

            if(digito1 != cpf.charAt(9) - '0') return false;

            soma = 0;

            for(int i = 0; i < 10; i++){
                soma += (cpf.charAt(i) - '0') * (11 - i);
            }

            resto = 11 - (soma%11);
            int digito2 = (resto >= 10) ? 0: resto;

            return digito2 == (cpf.charAt(10) - '0');
        } catch(Exception e){
            return false;
        }
    }

    public static boolean validarDuplicidadeCPF(List<? extends Pessoa> lista, String cpf){
        return lista.stream().anyMatch(p -> p.getCpf().equals(cpf));
    }
}
