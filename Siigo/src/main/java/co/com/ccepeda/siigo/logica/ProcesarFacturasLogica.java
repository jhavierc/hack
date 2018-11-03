/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.cron.ProcesarFacturaCron;
import co.com.ccepeda.siigo.dao.LogDAO;
import co.com.ccepeda.siigo.dto.ResponseModel;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.entities.Log;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author CarlosJavier
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ProcesarFacturasLogica {

    @Resource
    private UserTransaction userTransaction;

    @EJB
    private ProcesarFacturaCron procesarFacturaCron;
    @EJB
    private SiigoLogica siigoLogica;
    @EJB
    private LogDAO logDAO;
    @EJB
    private ProcesarDIANLogica procesarDIANLogica;
    @EJB
    private ConfirmacionDIANLogica confirmacionDIANLogica;

    private static final Logger LOG = Logger.getLogger(ProcesarFacturasLogica.class.getSimpleName());

    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.NEVER)
    public void procesarCargue(Factura factura, int hilo, long horaIniMillis, long horasMaxMillis) {
        long horaActualMillis;
        try {
            userTransaction.begin();

            while (factura != null) {

                LOG.log(Level.INFO, ">>>> Factura >>> hilo {0} procesando cargue {1}", new Object[]{hilo, factura.getFacId()});
                LOG.log(Level.INFO, "Tiempo inicio cargue {0}", System.currentTimeMillis());

                //Metodo que se encarga del procesamiento
                procesarInformacion(factura);
                //Una vez terminado marcar el cargue
                procesarFacturaCron.marcarFacturaProcesada(factura.getFacId(), hilo, factura.getCliente().getCliId());

                horaActualMillis = System.currentTimeMillis();
                LOG.log(Level.INFO, ">>>> INFO >>> Tiempo de procesamiento actual: ", horaActualMillis);
                if ((horaActualMillis - horaIniMillis) >= horasMaxMillis) {
                    factura = null;
                    LOG.log(Level.INFO, ">>>> INFO >>> Se excedio el tiempo maximo de ejecucion de la tarea, proceso suspendido.....");
                } else {
                    factura = procesarFacturaCron.obtenerSiguienteCargue(hilo);
                }
                LOG.log(Level.INFO, "Tiempo fin cargue {0}", System.currentTimeMillis());

            }
            userTransaction.commit();
            LOG.log(Level.INFO, "----------------------------------------------------------------------");
            LOG.log(Level.INFO, ">>>> INFO >>> finalizo el procesamiento del hilo {0}", hilo);
            LOG.log(Level.INFO, "----------------------------------------------------------------------");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, ">>>> ERROR  >> Error no controlado en el procesamiento!!!!, {0}", e.getLocalizedMessage());
            LOG.log(Level.SEVERE, ">>>> ERROR  >> Error no controlado en el procesamiento!!!!, {0}", e.getLocalizedMessage());
            try {
                if (userTransaction != null) {
                    userTransaction.rollback();
                }
            } catch (IllegalStateException ex) {
                LOG.log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                LOG.log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    private void procesarInformacion(Factura factura) {
        LOG.log(Level.INFO, "--PASO 2-- Factura {0}", factura.getFacId());

        ResponseModel responseModel = siigoLogica.singInvoice(factura);
        if (responseModel.isSuccess()) {
            LOG.log(Level.INFO, "--PASO 3-- Factura {0}", factura.getFacId());
            //Se lanza la tarea 3
            responseModel = siigoLogica.validateInvoice(factura);
            if (responseModel.isSuccess()) {
                LOG.log(Level.INFO, "--PASO 4-- Factura {0}", factura.getFacId());
                //Se lanza tarea 4, validacion DIAN
                responseModel = procesarDIANLogica.sendInvoice(factura);
                if (responseModel.isSuccess()) {
                    LOG.log(Level.INFO, "--PASO 5-- Factura {0}", factura.getFacId());
                    guardarTrazaFactura(factura, responseModel.getErrorList());
                    //LLamado asyncrono para confirmar factua DIAN
                    confirmacionDIANLogica.confirmState(factura);

                } else {
                    guardarTrazaFactura(factura, responseModel.getErrorList());
                }
            } else {
                guardarTrazaFactura(factura, responseModel.getErrorList());
            }
        } else {
            guardarTrazaFactura(factura, responseModel.getErrorList());
        }
    }

    private void guardarTrazaFactura(Factura factura, List<Object> errores) {

        if (errores != null) {
            for (Object errore : errores) {
                Log log = new Log();
                log.setLogId(UUID.randomUUID().toString());
                log.setLogFacId(factura);
                log.setLogStatus(factura.getFacState());
                log.setLogError(String.valueOf(errore));
                logDAO.create(log);
            }
        } else {
              Log log = new Log();
                log.setLogId(UUID.randomUUID().toString());
                log.setLogFacId(factura);
                log.setLogStatus(factura.getFacState());
                logDAO.create(log);
        }

    }
}
