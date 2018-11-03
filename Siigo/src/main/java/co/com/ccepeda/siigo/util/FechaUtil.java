/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.util;

import java.util.Date;

/**
 *
 * @author CarlosJavier
 */
public class FechaUtil {

    public static int diferenciaDeDosFechasEnDias(
            final Date fechaMayor,
            final Date fechaMenor) {
        long diferenciaEn_ms
                = fechaMayor.getTime() - fechaMenor.getTime();
        long dias = diferenciaEn_ms / (1000 * 60 * 60 * 24);
        return (int) dias;
    }

}
