/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

/**
 *
 * @author CarlosJavier
 */
import co.com.ccepeda.siigo.dao.ClienteDAO;
import co.com.ccepeda.siigo.dao.FacturaDAO;
import co.com.ccepeda.siigo.dao.LogDAO;
import co.com.ccepeda.siigo.dto.CorreoDTO;
import co.com.ccepeda.siigo.dto.InvoiceModel;
import co.com.ccepeda.siigo.dto.MensajeDTO;
import co.com.ccepeda.siigo.dto.ResponseModel;
import co.com.ccepeda.siigo.entities.Cliente;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.entities.Log;
import co.com.ccepeda.siigo.util.Constantes;
import co.com.ccepeda.siigo.util.EmailConstantes;
import co.com.ccepeda.siigo.util.TransformacionDozer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.binary.Base64;

@Stateless
public class SiigoLogica {

    @EJB
    private FacturaDAO facturaDAO;
    @EJB
    private ClienteDAO clienteDAO;
    @EJB
    private LogDAO logDAO;
    @EJB
    private NotificacionCorreoLogica notificacionCorreoLogica;

    private static final Logger LOG = Logger.getLogger(SiigoLogica.class.getSimpleName());

    /**
     * Lógica para la creacion de las facturas
     *
     * @param invoiceModel
     * @return
     */
    public Response saveInvoice(InvoiceModel invoiceModel) {
        Factura factura = null;
        boolean error = true;
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.ERROR.toString());
        String mensaje = "";
        boolean valido = true;
        if (invoiceModel.getCliente() == null || (invoiceModel.getCliente() != null && invoiceModel.getCliente().getId() == null)) {
            mensaje = "No se encontró información del cliente";
            valido = false;
        }

        if (valido) {
            Cliente cliente = clienteDAO.read(invoiceModel.getCliente().getId());
            if (cliente != null) {
                factura = TransformacionDozer.transformar(invoiceModel, Factura.class);
                facturaDAO.create(factura);

                if (invoiceModel.getPdfFile() != null) {

                    byte[] file = Base64.decodeBase64(invoiceModel.getPdfFile());
                    String ruta = guardarAdjunto(file, invoiceModel.getCliente().getId(), factura.getFacId());
                    if (ruta != null) {
                        factura.setFacUrlfile(ruta);
                        factura.setFacState(Constantes.InvoiceState.received.toString());
                        factura.setFacAction(Constantes.CustomerAction.noAction.toString());
                        factura.setFacCreateddate(new Date());
                        mensajeDTO.setCodigo(Constantes.StatusResponse.OK.toString());
                        mensaje = "Factura creada exitosamente con id {0}";
                        error = false;
                        guardarTrazaFactura(factura, Constantes.InvoiceState.received.toString(), "");
                        if (cliente.getCliCorreo() != null) {
                            notificacionCorreoLogica.enviarNotificacionCorreo(EmailConstantes.NOTIFICACION_SIIGO, MessageFormat.format(EmailConstantes.EMAIL_OK_SAVE_INVOICE, factura.getFacId().toString()), cliente.getCliCorreo());
                        }
                    } else {
                        mensaje = "No se pudo almacenar el archivo asociado a la factura";
                    }
                } else {
                    mensaje = "Debe adjuntar el pdf";
                }
            } else {
                mensaje = "El cliente no se encuentra registeado en el sistema";
            }
        }
        if (error) {
            mensajeDTO.setMensaje(mensaje);
            if (factura != null) {
                guardarTrazaFactura(factura, Constantes.InvoiceState.received.toString(), mensaje);
            }

        } else {
            mensajeDTO.setMensaje(MessageFormat.format(mensaje, factura != null ? factura.getFacId() : "-1"));
        }
        return Response.ok(mensajeDTO, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Funcionalidad para fimar una peticion con el certificado
     *
     * @param idinvoice
     * @return
     */
    public ResponseModel singInvoice(Factura factura) {

        final Integer CONSTSeed = 100;
        final Integer CONSTSleepValue = 2000;
        final double CONSTErrorProb = 0.1;
        List<Object> errorList = new ArrayList<Object>();
        ResponseModel responseModel = new ResponseModel();

        LOG.log(Level.INFO, "==== Sign adjunto=====");
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.ERROR.toString());

        if (factura != null) {
            if (factura.getCliente().getCliCertificado() != null) {
                mensajeDTO.setMensaje("Para el final");

                Random random = new Random(CONSTSeed);
                if (random.nextDouble() <= CONSTErrorProb) {
                    // [TODO] Implementar cambio de estado por fallo de firma y notificar al administrador del sistema

                    factura.setFacState(Constantes.InvoiceState.signError.toString());
                    guardarTrazaFactura(factura, Constantes.InvoiceState.signError.toString(), "");
                    responseModel.setSuccess(false);
                    errorList.add("Error al firmar el documento");
                    responseModel.setErrorList(errorList);
                    notificacionCorreoLogica.enviarNotificacionCorreo(EmailConstantes.NOTIFICACION_SIIGO, EmailConstantes.EMAIL_ERROR_SING_FACTURA, "admin@siigo.com");
                } else {
                    // [TODO] Implementar actualizacion de UBL de factura asumiendo firma exitosa y cambio de estado
                    factura.setFacState(Constantes.InvoiceState.signed.toString());
                    guardarTrazaFactura(factura, Constantes.InvoiceState.signed.toString(), "");
                    responseModel.setSuccess(true);

                }
                facturaDAO.update(factura);
            } else {
                mensajeDTO.setMensaje("El cliente no tiene parametrizado el Certificado");
            }
        } else {
            mensajeDTO.setMensaje("La factura con id {0}, no se encuentra registrada");
        }
        return responseModel;
    }

    public Response sendInvoice(InvoiceModel invoiceModel) {
        LOG.log(Level.INFO, "==== Send adjunto=====");
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.ERROR.toString());

        mensajeDTO.setMensaje("Para el final");
        return Response.ok(mensajeDTO, MediaType.APPLICATION_JSON).build();
    }

    /**
     * FUncionalida que permite guardar un archivo en un fileSistem
     *
     * @param pdf
     * @param idcliente
     * @param idfactura
     * @return
     */
    private String guardarAdjunto(byte[] pdf, final Long idcliente, final Long idfactura) {
        LOG.log(Level.INFO, "==== Guardando adjunto=====");
        String path = null;

        try {
            path = generarUrlArchivo(pdf, idcliente, idfactura);
            LOG.log(Level.INFO, ">>> Path archivo pdf >> {0}", path);
            FileOutputStream fileOut = new FileOutputStream(path + "\\adjunto.pdf");
            BufferedOutputStream buffer = new BufferedOutputStream(fileOut);
            buffer.write(pdf);
            buffer.flush();
            buffer.close();
            fileOut.close();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al guardar el archivo adjunto {0}", e.getCause());
        }
        return path;

    }

    /**
     * Funcionalidad que permite generar la url del archivo a guardar
     *
     * @param pdf Bytes del archivo
     * @param idcliente Identificador del cliente
     * @param idfactura Identificador de la factura asociada
     * @return Url del path generado
     */
    private String generarUrlArchivo(byte[] pdf, final Long idcliente, final Long idfactura) {
        String pathBase = "D:\\SIIGO\\FileSistem\\";
        Date fechaSistema = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaSistema);
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);

        StringBuilder path = new StringBuilder();
        path.append(pathBase).append(anio);

        File fileSystem = new File(path.toString());
        if (!fileSystem.exists()) {
            fileSystem.mkdir();
        }
        path.append("\\").append(mes);
        fileSystem = new File(path.toString());
        if (!fileSystem.exists()) {
            fileSystem.mkdir();
        }

        path.append("\\").append(idcliente.toString());
        fileSystem = new File(path.toString());
        if (!fileSystem.exists()) {
            fileSystem.mkdir();
        }
        path.append("\\").append(idfactura.toString());
        fileSystem = new File(path.toString());
        if (!fileSystem.exists()) {
            fileSystem.mkdir();
        }
        return path.append("\\").toString();
    }

    private void guardarTrazaFactura(Factura factura, String status, String error) {
        Log log = new Log();
        log.setLogId(UUID.randomUUID().toString());
        log.setLogFacId(factura);
        log.setLogStatus(status);
        log.setLogError(error);
        logDAO.create(log);
    }

   

    public static ResponseModel validateInvoice(Factura invoice) {
        List<Object> errorList = new ArrayList<Object>();
        ResponseModel responseModel = new ResponseModel();
        
        boolean valido = true;
        if(invoice.getFacPrefix()==null || invoice.getFacPrefix().length()==0){
            errorList.add("El prefix es obligatorio");
            valido = false;
        }
        if(invoice.getFacConsecutive()==null){
            errorList.add("El consecutive es obligatorio");
            valido = false;
        }
        if(invoice.getFacUbl()==null || invoice.getFacUbl().length()==0){
            errorList.add("El UBL es obligatorio");
            valido = false;
        }
        responseModel.setSuccess(valido);
        responseModel.setErrorList(errorList);
       
        return responseModel;
    }
}
