/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.dto;

import co.com.ccepeda.siigo.util.Constantes;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author CarlosJavier
 */
@Data
@NoArgsConstructor
public class InvoiceModel {
    
    public Long id;
    public String prefix;
    public long consecutive;
    public String ubl;
    public String pdfFile; //Base64
    public String createddate;
    public Constantes.InvoiceState state;
    public Constantes.CustomerSendState customerSendState;
    public Constantes.CustomerAction customerAction;
    public ClienteDTO cliente;
    
    public boolean tieneAdjunto;
    
}
