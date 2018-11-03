/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.util.Constantes;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.auth.AuthMethod;
import com.nexmo.client.auth.TokenAuthMethod;
import com.nexmo.client.sms.SmsSubmissionResult;
import com.nexmo.client.sms.messages.TextMessage;
import java.util.logging.Logger;
import javax.ejb.Stateless;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class EnviarSMSLogica {

    
    private static final Logger LOG = Logger.getLogger(EnviarSMSLogica.class.getSimpleName());
    
    /**
     * FUncionalidad que permite enviar mensajes de texto
     * @param celular
     * @param mensaje 
     */
    public void enviarMensaje(String celular, String mensaje) {
        try {
            //Los parámetros del API se deben sacar de una tabla de constantes
            AuthMethod auth = new TokenAuthMethod("a6c2bcc2 (Master)", "b8Wu0puax1BFSIwB");
            NexmoClient client = new NexmoClient(auth);

            SmsSubmissionResult[] responses = client.getSmsClient().submitMessage(new TextMessage(Constantes.SIIGO, celular, mensaje));

            for (SmsSubmissionResult response : responses) {
                System.out.println(response);
            }
        } catch (Exception e) {
        }

    }

}
