/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.dao;

import co.com.ccepeda.siigo.entities.Factura;
import java.util.List;
import javax.ejb.Stateless;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class FacturaDAO extends GenericoDAO<Factura>{
    
    public FacturaDAO(){
        super(Factura.class);
    }
    
    public List<Factura> consultarFacturasCargadas(){
        return this.filtrar("Factura.findByFacStateReceived", null);
    }
}
