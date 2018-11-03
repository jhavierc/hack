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
import java.io.File;
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

    public Response saveInvoice(InvoiceModel invoiceModel) {
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.OK.toString());

        Factura factura = TransformacionDozer.transformar(invoiceModel, Factura.class);
        facturaDAO.create(factura);

        String mensaje;
        if (invoiceModel.getPdfFile() != null) {

            byte[] file = Base64.decodeBase64(invoiceModel.getPdfFile());
            String ruta = guardarAdjunto(file, invoiceModel.getCliente().getId(), factura.getFacId());
            factura.setFacUrlfile(ruta);
            
            
            
            

        } else {
            mensaje = "Debe adjuntar el pdf";
        }
        return Response.ok(mensajeDTO, MediaType.APPLICATION_JSON).build();
    }

    public Response sendInvoice(InvoiceModel invoiceModel) {
        ResponseModel responseModel = new ResponseModel();
        return Response.ok(responseModel, MediaType.APPLICATION_JSON).build();
    }

    private String guardarAdjunto(byte[] pdf, final Long idcliente, final Long idfactura) {
        LOG.log(Level.INFO, "==== Guardando adjunto=====");
        String path = generarUrlArchivo(pdf, idcliente, idfactura);
        
        return path;

    }

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
