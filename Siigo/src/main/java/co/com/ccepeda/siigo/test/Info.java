/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.test;

/**
 *
 * @author CarlosJavier
 */
public class Info {
    
    private String letra;
    private Integer posicion;

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public Integer getPosicion() {
        return posicion;
    }

    public void setPosicion(Integer posicion) {
        this.posicion = posicion;
    }
    
    public Info(String letra, Integer posicion){
        this.letra = letra;
        this.posicion = posicion;
    }
        
}
