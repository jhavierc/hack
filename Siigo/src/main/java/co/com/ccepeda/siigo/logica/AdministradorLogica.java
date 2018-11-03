/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.dao.FacturaDAO;
import co.com.ccepeda.siigo.dao.LogDAO;
import co.com.ccepeda.siigo.dto.LogDTO;
import co.com.ccepeda.siigo.dto.MensajeDTO;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.entities.Log;
import co.com.ccepeda.siigo.util.Constantes;
import co.com.ccepeda.siigo.util.TransformacionDozer;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class AdministradorLogica {

    @EJB
    private FacturaDAO facturaDAO;
    @EJB
    private ConfirmacionDIANLogica confirmacionDIANLogica;
    @EJB
    private LogDAO logDAO;

    public Response reprocesarInfoDian() {
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.OK.toString());

        List<Factura> listaFacturas = facturaDAO.consultarFacturasPendienteDIAN();
        if (listaFacturas != null && !listaFacturas.isEmpty()) {
            for (Factura factura : listaFacturas) {
                confirmacionDIANLogica.confirmState(factura);
            }
            mensajeDTO.setMensaje("El procesamiento de las facturas retenidas está en proceso");
        } else {
            mensajeDTO.setMensaje("No hay facturas pendiente de validación con la DIAN");
        }
        return Response.ok(mensajeDTO, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Permite consultar los logs por cantidad de registros en base de datos
     * @param cantidad
     * @return 
     */
    public Response consultarLogsCantidad(Integer cantidad) {
        MensajeDTO mensajeDTO = new MensajeDTO();
        mensajeDTO.setCodigo(Constantes.StatusResponse.OK.toString());

        List<LogDTO> listaLogs = new ArrayList<>();
        List<Log> logs = logDAO.consultarLogsPorCantidad(cantidad);
        if (logs != null && !logs.isEmpty()) {
            for (Log log : logs) {
                LogDTO logdata = TransformacionDozer.transformar(log, LogDTO.class);
                logdata.setIdfactura(log.getLogFacId().getFacId());
                listaLogs.add(logdata);
            }
            mensajeDTO.setObject(listaLogs);
        } else {
            mensajeDTO.setMensaje("No hay registros en el LOg");
        }
        return Response.ok(mensajeDTO, MediaType.APPLICATION_JSON).build();
    }

}
