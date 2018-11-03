/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

/**
 *
 * Objeto de respuesta a los servicios
 * @author CarlosJavier
 */
@Data
@NoArgsConstructor
public class ResponseModel {

    public boolean success;
    public String CUFE;
    public List<Object> errorList;

}
