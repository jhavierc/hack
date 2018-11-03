/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.services;

import co.com.ccepeda.siigo.dto.InvoiceModel;
import co.com.ccepeda.siigo.logica.SiigoLogica;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author CarlosJavier
 */
@Path("siigoServices")
@Stateless
public class SiigoServices {

    @EJB
    private SiigoLogica siigoLogica;

    /**
     * Servicio que permite registrar una factura
     *
     * @param invoiceModel
     * @return
     */
    @POST
    @Path("/SaveInvoice")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveInvoice(InvoiceModel invoiceModel) {
        return siigoLogica.saveInvoice(invoiceModel);
    }

    /**
     * Servicio que permite firmar un archivo
     * @param id Identificador de la factura a firmar
     * @return 
     */
    @GET
    @Path("/SignInvoice")
    public Response saveInvoice(@PathParam("id") Long id) {
        return siigoLogica.singInvoice(id);
    }
}
