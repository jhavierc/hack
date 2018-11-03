/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.dao.FacturaDAO;
import co.com.ccepeda.siigo.dao.LogDianDAO;
import co.com.ccepeda.siigo.dto.ResponseModel;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.entities.LogDian;
import co.com.ccepeda.siigo.util.Constantes;
import co.com.ccepeda.siigo.util.EmailConstantes;
import co.com.ccepeda.siigo.util.FechaUtil;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class ProcesarDIANLogica {

    static int CONSTSeed = 100;
    static int CONSTSleepValue = 2000;
    static double CONSTErrorProb = 0.1;

    @EJB
    private FacturaDAO facturaDAO;
    @EJB
    private NotificacionCorreoLogica notificacionCorreoLogica;
    @EJB
    private LogDianDAO logDianDAO;

    @EJB
    private DianServicioLogica dianServicioLogica;
    @EJB
    private EnviarSMSLogica enviarSMSLogica;

    /**
     * Funcionalidad para enviar la factura a la DIAN
     *
     * @param invoice
     * @return
     */
    public ResponseModel sendInvoice(Factura invoice) {
        boolean valido = false;
        ResponseModel responseModel = new ResponseModel();
        try {
            Thread.sleep(CONSTSleepValue);
            try {
                String result = dianServicioLogica.receive(invoice.toString());
                if (result.equals("ok")) {
                    // [TODO] Implementar cambio de estado por envio exitoso
                    responseModel.setSuccess(true);
                    invoice.setFacState(Constantes.InvoiceState.sended.toString());
                    valido = true;
                } else {
                    // [TODO] Implementar cambio de estado por envio fallido y notificar al obligado y al administrador
                    responseModel.setSuccess(false);
                    invoice.setFacState(Constantes.InvoiceState.sendError.toString());
                }
            } catch (Exception e) {
                validarNotificacionWsDIANNoDisponible(e);
                // [TODO] Implementar cambio de estado por envio fallido y notificar al obligado y al administrador
                responseModel.setSuccess(false);
                invoice.setFacState(Constantes.InvoiceState.sendError.toString());
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcesarDIANLogica.class.getName()).log(Level.SEVERE, null, ex);
        }
        facturaDAO.update(invoice);
        if (!valido) {
            notificacionCorreoLogica.enviarNotificacionCorreo(EmailConstantes.NOTIFICACION_SIIGO,
                    MessageFormat.format(EmailConstantes.EMAIL_ERROR_SEND_FACTURA_DIAN, invoice.getFacId().toString()), "admin@siigo.com");
            enviarSMSLogica.enviarMensaje(Constantes.NUMERO_CELULAR, Constantes.MENSAJE);
        }
        return responseModel;
    }

    /**
     * Permite validar si ya pasaron 24 horas de la caida del servicio de la
     * DIAN
     *
     * @param exception
     */
    private void validarNotificacionWsDIANNoDisponible(Exception exception) {
        if (exception != null) {
            Date fecha = new Date();
            if (exception.getMessage().equals("Service no available")) {
                LogDian logDian = logDianDAO.consultarUltimaCaida();
                if (logDian != null) {
                    Integer dato = FechaUtil.diferenciaDeDosFechasEnDias(fecha, logDian.getLogdianUltimacaida());
                    if (dato >= 1) {
                        notificacionCorreoLogica.enviarNotificacionCorreo(EmailConstantes.NOTIFICACION_SIIGO, EmailConstantes.EMAIL_ERROR_NOTIFICACION_DIAN, "admin@siigo.com");
                         enviarSMSLogica.enviarMensaje(Constantes.NUMERO_CELULAR, Constantes.MENSAJE);
                        logDian.setLogdianEstado("INACTIVO");
                        logDianDAO.update(logDian);
                    }
                    //se valida 24 horas con respecto a la fecha del sistema
                }
            } else {
                LogDian logDian = new LogDian();
                logDian.setLogdianEstado("ACTIVO");
                logDian.setLogdianUltimacaida(fecha);
                logDianDAO.create(logDian);
            }
        }
    }
}
