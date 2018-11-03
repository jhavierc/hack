/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.logica;

import java.util.Random;
import javax.ejb.Stateless;

/**
 *
 * @author CarlosJavier
 */
@Stateless
public class DianServicioLogica {

    static int CONSTSeed = 100;
    static int CONSTSleepValue = 1000;
    static double CONSTErrorProb = 0.1;
    static double CONSTSystemDownProb = 0.1;

    public String receive(String invoice) throws Exception {
        Random random = new Random(CONSTSeed);
        Thread.sleep(CONSTSleepValue);
        if (random.nextDouble() <= CONSTSystemDownProb) {
            throw new Exception("Service no available");
        }
        if (random.nextDouble() <= CONSTErrorProb) {
            return "error";
        } else {
            return "ok";
        }
    }

    public String getInvoiceState(String invoice) throws Exception {
        Random random = new Random(CONSTSeed);
        Thread.sleep(CONSTSleepValue);
        if (random.nextDouble() <= CONSTSystemDownProb) {
            throw new Exception("Service no available");
        }
        if (random.nextDouble() <= CONSTErrorProb) {
            return "error";
        } else {
            return "ok";
        }
    }

}
