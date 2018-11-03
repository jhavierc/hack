/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author CarlosJavier
 */
@Entity
@Table(name = "tbl_logdian")
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "LogDian.findAll", query = "SELECT l FROM LogDian l")
    , @NamedQuery(name = "LogDian.findByUltimaCaidaActiva", query = "SELECT l FROM LogDian l WHERE l.logdianId = (SELECT MAX(l.logdianId) FROM LogDian WHERE l.logdianEstado='ACTIVO')")
})
public class LogDian implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "logdian_id")
    private BigDecimal logdianId;
   
    @Column(name = "logdian_ultimacaida")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logdianUltimacaida;
   
    @Column(name = "logdian_estado")
    private String logdianEstado;

    public BigDecimal getLogdianId() {
        return logdianId;
    }

    public void setLogdianId(BigDecimal logdianId) {
        this.logdianId = logdianId;
    }

    public Date getLogdianUltimacaida() {
        return logdianUltimacaida;
    }

    public void setLogdianUltimacaida(Date logdianUltimacaida) {
        this.logdianUltimacaida = logdianUltimacaida;
    }

    public String getLogdianEstado() {
        return logdianEstado;
    }

    public void setLogdianEstado(String logdianEstado) {
        this.logdianEstado = logdianEstado;
    }

    
}
