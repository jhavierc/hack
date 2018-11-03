/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.util;

/**
 *
 * @author CarlosJavier
 */
public class Constantes {
    
    public enum StatusResponse{
        OK,ERROR
    }

    public enum InvoiceState {
        received,
        signed,
        signError,
        validated,
        validationError,
        sended,
        sendError,
        confirmed,
        confirmError
    }

    public enum CustomerSendState {
        noSended,
        sended,
        sendError
    }

    public enum CustomerAction {
        noAction,
        approved,
        rejected
    }
}
