/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author CarlosJavier
 */
@Entity
@Table(name = "tbl_log")
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l")
    , @NamedQuery(name = "Log.findByLogId", query = "SELECT l FROM Log l WHERE l.logId = :logId")
    , @NamedQuery(name = "Log.findByIdfactura", query = "SELECT l FROM Log l WHERE l.logFacId.facId = :idfac")})
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "log_id")
    private String logId;

    @Column(name = "log_status")
    private String logStatus;

    @JoinColumn(name = "log_fac_id", referencedColumnName = "fac_id")
    @ManyToOne(optional = false)
    private Factura logFacId;

    @Column(name = "log_error")
    private String logError;

    public Log() {
    }

    public Log(String logId) {
        this.logId = logId;
    }

    public Log(String logId, String logStatus) {
        this.logId = logId;
        this.logStatus = logStatus;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getLogStatus() {
        return logStatus;
    }

    public void setLogStatus(String logStatus) {
        this.logStatus = logStatus;
    }

    public Factura getLogFacId() {
        return logFacId;
    }

    public void setLogFacId(Factura logFacId) {
        this.logFacId = logFacId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (logId != null ? logId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Log)) {
            return false;
        }
        Log other = (Log) object;
        if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.ccepeda.siigo.entities.Log[ logId=" + logId + " ]";
    }

    public String getLogError() {
        return logError;
    }

    public void setLogError(String logError) {
        this.logError = logError;
    }

}
