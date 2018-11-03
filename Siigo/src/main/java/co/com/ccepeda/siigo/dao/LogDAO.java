/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.dao;

import co.com.ccepeda.siigo.entities.Log;
import javax.ejb.Stateless;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class LogDAO extends  GenericoDAO<Log>{
    
    public LogDAO(){
        super(Log.class);
    }
}
