/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.util;

/**
 *
 * @author CarlosJavier
 */
public final class EmailConstantes {
    
    private EmailConstantes(){
        
    }
    public static final String NOTIFICACION_SIIGO ="NOTIFICACIONES SIIGO";
    public static final String EMAIL_ERROR_SING_FACTURA="Señ@r administrador, se presentó un error al firmar la factura N° {0}"; 
    public static final String EMAIL_ERROR_SEND_FACTURA_DIAN="Señ@r administrador, la factura N° {0}, presentó un error al enviar la información a la DIAN"; 
    public static final String EMAIL_ERROR_NOTIFICACION_DIAN="Señ@r administrador, el servicio de la DIAN no está disponible"; 
            
    public static final String EMAIL_ERROR_SAVE_INVOICE ="Señ@r usuario, no se puedo guardar la información de la factura";
    public static final String EMAIL_OK_SAVE_INVOICE ="Señ@r usuario, se registró la factura con id {0}. El sistema le notificará cuando termine de realizar la validación de la información.<br>Puede realizar la descarga del archivo adjunto, ingresando al siguien te link <a src=\"http://localhost:4200/api/siigoServices/adjunto/{1}.pdf\">Descargar</a>";
}
