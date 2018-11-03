/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import co.com.ccepeda.siigo.dao.FacturaDAO;
import co.com.ccepeda.siigo.dto.MensajeDTO;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.util.Constantes;
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

}
