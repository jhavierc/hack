/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author CarlosJavier
 */
@Data
@NoArgsConstructor
public class MensajeDTO {

    private String codigo;
    private String mensaje;
    private Object object;

}
