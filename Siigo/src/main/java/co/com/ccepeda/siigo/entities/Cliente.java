/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author CarlosJavier
 */
@Entity
@Table(name = "tbl_cliente")
@Cacheable(false)
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")
    , @NamedQuery(name = "Cliente.findByCliId", query = "SELECT c FROM Cliente c WHERE c.cliId = :cliId")
    , @NamedQuery(name = "Cliente.findByCliNit", query = "SELECT c FROM Cliente c WHERE c.cliNit = :cliNit")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @SequenceGenerator(name = "SeqClienteId", sequenceName = "tbl_cliente_seq", allocationSize = 1)
    @GeneratedValue(generator = "SeqClienteId", strategy = GenerationType.SEQUENCE)
    @Column(name = "cli_id")
    private Long cliId;
    
    @Column(name = "cli_nit")
    private String cliNit;
    
    @Column(name = "cli_certificado")
    private byte[] cliCertificado;
    
    @Column(name = "cli_correo")
    private String cliCorreo;

    public Long getCliId() {
        return cliId;
    }

    public void setCliId(Long cliId) {
        this.cliId = cliId;
    }

    public String getCliNit() {
        return cliNit;
    }

    public void setCliNit(String cliNit) {
        this.cliNit = cliNit;
    }

    public byte[] getCliCertificado() {
        return cliCertificado;
    }

    public void setCliCertificado(byte[] cliCertificado) {
        this.cliCertificado = cliCertificado;
    }

    public String getCliCorreo() {
        return cliCorreo;
    }

    public void setCliCorreo(String cliCorreo) {
        this.cliCorreo = cliCorreo;
    }

    
}
