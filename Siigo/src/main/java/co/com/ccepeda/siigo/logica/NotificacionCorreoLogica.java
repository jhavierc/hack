/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.dto.CorreoDTO;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class NotificacionCorreoLogica {

    @EJB
    private EnvioCorreoLogica envioCorreoLogica;

    public void enviarNotificacionCorreo(String titulo, String mensaje, String correo) {
        CorreoDTO correoDTO = new CorreoDTO();
        correoDTO.setAsunto(titulo);
        correoDTO.setDestino(correo);
        correoDTO.setMensaje(mensaje);
        //Estos datos se pueden sacar desde unas constantes en base de datos
        correoDTO.setUserCorreo("notify@siggo.com");
        correoDTO.setPassword("contreasenia");
        envioCorreoLogica.sendEmail(correoDTO);
    }

}
