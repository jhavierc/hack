/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author CarlosJavier
 */
@Entity
@Table(name = "tbl_factura")
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f")
    , @NamedQuery(name = "Factura.findByFacId", query = "SELECT f FROM Factura f WHERE f.facId = :facId")
    , @NamedQuery(name = "Factura.findByFacPrefix", query = "SELECT f FROM Factura f WHERE f.facPrefix = :facPrefix")
    , @NamedQuery(name = "Factura.findByFacConsecutive", query = "SELECT f FROM Factura f WHERE f.facConsecutive = :facConsecutive")
    , @NamedQuery(name = "Factura.findByFacUbl", query = "SELECT f FROM Factura f WHERE f.facUbl = :facUbl")
    , @NamedQuery(name = "Factura.findByFacUrlfile", query = "SELECT f FROM Factura f WHERE f.facUrlfile = :facUrlfile")
    , @NamedQuery(name = "Factura.findByFacCreateddate", query = "SELECT f FROM Factura f WHERE f.facCreateddate = :facCreateddate")
    , @NamedQuery(name = "Factura.findByFacState", query = "SELECT f FROM Factura f WHERE f.facState = :facState")
    , @NamedQuery(name = "Factura.findByFacSendstate", query = "SELECT f FROM Factura f WHERE f.facSendstate = :facSendstate")
    , @NamedQuery(name = "Factura.findByFacAction", query = "SELECT f FROM Factura f WHERE f.facAction = :facAction")})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SeqFacturaId", sequenceName = "tbl_factura_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqFacturaId", strategy = GenerationType.SEQUENCE)
    @Column(name = "fac_id")
    private Long facId;
    
    @Column(name = "fac_prefix")
    private String facPrefix;
    
    @Column(name = "fac_consecutive")
    private BigInteger facConsecutive;
    
    @Column(name = "fac_ubl")
    private String facUbl;
    
    @Column(name = "fac_urlfile")
    private String facUrlfile;
    
    @Column(name = "fac_createddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date facCreateddate;
    
    @Column(name = "fac_state")
    private String facState;
    
    @Column(name = "fac_sendstate")
    private String facSendstate;
    
    @Column(name = "fac_action")
    private String facAction;
    
    @JoinColumn(name = "fac_cli_id", referencedColumnName = "cli_id")
    @ManyToOne(optional = false)
    private Cliente cliente;

    public Long getFacId() {
        return facId;
    }

    public void setFacId(Long facId) {
        this.facId = facId;
    }

    public String getFacPrefix() {
        return facPrefix;
    }

    public void setFacPrefix(String facPrefix) {
        this.facPrefix = facPrefix;
    }

    public BigInteger getFacConsecutive() {
        return facConsecutive;
    }

    public void setFacConsecutive(BigInteger facConsecutive) {
        this.facConsecutive = facConsecutive;
    }

    public String getFacUbl() {
        return facUbl;
    }

    public void setFacUbl(String facUbl) {
        this.facUbl = facUbl;
    }

    public String getFacUrlfile() {
        return facUrlfile;
    }

    public void setFacUrlfile(String facUrlfile) {
        this.facUrlfile = facUrlfile;
    }

    public Date getFacCreateddate() {
        return facCreateddate;
    }

    public void setFacCreateddate(Date facCreateddate) {
        this.facCreateddate = facCreateddate;
    }

    public String getFacState() {
        return facState;
    }

    public void setFacState(String facState) {
        this.facState = facState;
    }

    public String getFacSendstate() {
        return facSendstate;
    }

    public void setFacSendstate(String facSendstate) {
        this.facSendstate = facSendstate;
    }

    public String getFacAction() {
        return facAction;
    }

    public void setFacAction(String facAction) {
        this.facAction = facAction;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
