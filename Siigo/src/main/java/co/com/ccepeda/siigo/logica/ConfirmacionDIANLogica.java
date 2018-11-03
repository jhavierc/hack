/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.dao.FacturaDAO;
import co.com.ccepeda.siigo.dto.ResponseModel;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.util.Constantes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

/**
 *
 * @author CarlosJavier
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ConfirmacionDIANLogica {

    static int CONSTSeed = 100;
    static int CONSTSleepValue = 2000;
    static int CONT_REINTENTO = 2000;//5 minutos 300000
    
    private static final Logger LOG = Logger.getLogger(ConfirmacionDIANLogica.class.getSimpleName());

    @EJB
    private DianServicioLogica dianServicioLogica;
    @EJB
    private FacturaDAO facturaDAO;

    /**
     * Método asincrono para ejecutar validación de confirmación con el servicio
     * de la DIAN
     *
     * @param invoice
     * @return
     */
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void confirmState(Factura invoice) {
        
        LOG.log(Level.INFO,"====ConfirmacionDIANLogica ==> confirmState ==> {0} ",invoice.getFacId());
                
        int cantidad = 0;
        ResponseModel responseModel = new ResponseModel();
        responseModel.setSuccess(false);
        try {
            while (!responseModel.isSuccess()) {
                Thread.sleep(CONT_REINTENTO);

                String result = dianServicioLogica.getInvoiceState(invoice.toString());
                if (!result.equals("ok")) {
                    cantidad++;
                } else {
                    //Si responde, cambia de estado
                    responseModel.setSuccess(true);
                    invoice.setFacState(Constantes.InvoiceState.confirmed.toString());
                }
                //Se realizan 5 intentos a la DIAN para validar una Factura
                if (cantidad == 5) {
                    responseModel.setSuccess(false);
                    invoice.setFacState(Constantes.InvoiceState.confirmError.toString());
                    break;
                }
            }
        } catch (Exception e) {
            // [TODO] Notificar al obligado y al administrador del error de consulta de estado
            responseModel.setSuccess(false);
            invoice.setFacState(Constantes.InvoiceState.confirmError.toString());
        }
        facturaDAO.update(invoice);
        //return responseModel;
    }

}
