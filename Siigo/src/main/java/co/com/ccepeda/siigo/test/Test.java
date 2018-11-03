/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CarlosJavier
 */
public class Test {
    
    public static void main(String[] args) {
         List<Info> letras = new ArrayList<>();
        letras.add(new Info(" ", 0));
        letras.add(new Info("A", 1));
        letras.add(new Info("B", 2));
        letras.add(new Info("C", 3));
        letras.add(new Info("D", 4));
        letras.add(new Info("F", 5));
        letras.add(new Info("F", 6));
        letras.add(new Info("G", 7));
        letras.add(new Info("H", 8));
        letras.add(new Info("I", 9));
        letras.add(new Info("J", 10));
        letras.add(new Info("K", 11));
        letras.add(new Info("L", 12));
        letras.add(new Info("M", 13));
        letras.add(new Info("N", 14));
        letras.add(new Info("O", 15));
        letras.add(new Info("P", 16));
        letras.add(new Info("Q", 17));
        letras.add(new Info("R", 18));
        letras.add(new Info("S", 19));
        letras.add(new Info("T", 20));
        letras.add(new Info("U", 21));
        letras.add(new Info("V", 22));
        letras.add(new Info("W", 23));
        letras.add(new Info("X", 24));
        letras.add(new Info("Y", 25));
        letras.add(new Info("Z", 26));

        String texto = "SIIGO ";
        Integer total=0;
        for (int i = 0; i < texto.length(); i++) {
            char letra = texto.charAt(i);
            System.out.println("letra >>"+letra+" >> pos "+i);
            total = total + calculo(String.valueOf(letra),letras,i+1);
            
            //Tratamiento del caracter
        }
        System.out.println("Valor cifrado = " +total);

    }
 
    private static Integer calculo(String letra, List<Info> letras, Integer posicion) {
        Integer calulo = 0;
        
            //System.out.println("--------------");
            for (Info info : letras) {
                if (info.getLetra().equals(letra)) {
                    System.out.println("****letra >>"+info.getLetra()+" >> pos "+info.getPosicion() +">>>> posicion texto "+posicion);
                    calulo = calulo + (info.getPosicion() * posicion);
                }
            }
        
        return calulo;
    }
}
