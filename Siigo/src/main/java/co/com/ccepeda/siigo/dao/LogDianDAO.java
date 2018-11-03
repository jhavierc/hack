/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.dao;

import co.com.ccepeda.siigo.entities.LogDian;
import javax.ejb.Stateless;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class LogDianDAO extends GenericoDAO<LogDian> {

    public LogDianDAO() {
        super(LogDian.class);
    }

    public LogDian consultarUltimaCaida(){
        return this.filtrarUnicoRegistro("LogDian.findByUltimaCaidaActiva", null);
    }
}
