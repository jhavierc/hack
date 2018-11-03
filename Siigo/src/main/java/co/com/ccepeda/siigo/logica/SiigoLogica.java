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
import co.com.ccepeda.siigo.dto.InvoiceModel;
import co.com.ccepeda.siigo.dto.MensajeDTO;
import co.com.ccepeda.siigo.dto.ResponseModel;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.util.Constantes;
import co.com.ccepeda.siigo.util.TransformacionDozer;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
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

    private static final Logger LOG = Logger.getLogger(SiigoLogica.class.getSimpleName());

    /**
     * Lógica para la creacion de las facturas
     *
     * @param invoiceModel
     * @return
     */
    public Response saveInvoice(InvoiceModel invoiceModel) {

        boolean error = true;
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.ERROR.toString());
        Factura factura = TransformacionDozer.transformar(invoiceModel, Factura.class);
        facturaDAO.create(factura);

        String mensaje = "";
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
            } else {
                mensaje = "No se pudo almacenar el archivo asociado a la factura";
            }
        } else {
            mensaje = "Debe adjuntar el pdf";
        }
        if (error) {
            mensajeDTO.setMensaje(mensaje);
        } else {
            mensajeDTO.setMensaje(MessageFormat.format(mensaje, factura.getFacId()));
        }
        return Response.ok(mensajeDTO, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Funcionalidad para fimar una peticion con el certificado
     * @param idinvoice
     * @return 
     */
    public Response singInvoice(Long idinvoice) {
        
        LOG.log(Level.INFO, "==== Sign adjunto=====");
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.ERROR.toString());
        
        Factura factura = facturaDAO.read(idinvoice);
        if(factura!=null){
            if(factura.getCliente().getCliCertificado()!=null){
                mensajeDTO.setMensaje("Para el final");
            } else {
                mensajeDTO.setMensaje("El cliente no tiene parametrizado el Certificado");
            }
        } else {
            mensajeDTO.setMensaje(MessageFormat.format("La factura con id {0}, no se encuentra registrada", idinvoice));
        }
        return Response.ok(mensajeDTO, MediaType.APPLICATION_JSON).build();
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
}
