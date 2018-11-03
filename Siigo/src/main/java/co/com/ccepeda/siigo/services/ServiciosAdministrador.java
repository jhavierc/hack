/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.services;

import co.com.ccepeda.siigo.logica.AdministradorLogica;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("serviciosAdministrador")
@Stateless
public class ServiciosAdministrador {

    @EJB

    private AdministradorLogica administradorLogica;

    /**
     * Retrieves representation of an instance of
     * co.com.ccepeda.siigo.services.ServiciosAdministrador
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/procesar/dian")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reprocesarInfoDian() {
        return administradorLogica.reprocesarInfoDian();
    }
    
    @GET
        @Path("/monitor/{cantidad}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarLogsCantidad(@PathParam("cantidad") Integer cantidad) {
        return administradorLogica.consultarLogsCantidad(cantidad);
    }
}
