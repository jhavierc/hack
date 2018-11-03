/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.ccepeda.siigo.cron;

import co.com.ccepeda.siigo.dao.FacturaDAO;
import co.com.ccepeda.siigo.entities.Factura;
import co.com.ccepeda.siigo.logica.ProcesarFacturasLogica;
import co.com.ccepeda.siigo.logica.SiigoLogica;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

/**
 *
 * @author CarlosJavier
 */
@Singleton
public class ProcesarFacturaCron {

    @EJB
    private FacturaDAO facturaDAO;
    @EJB
    private ProcesarFacturasLogica procesarFacturasLogica;

    private ConcurrentHashMap<Long, Integer> hilos = new ConcurrentHashMap<>();
    private List<Factura> listaFacturas = new ArrayList<Factura>();

    private void consultarFacturas() {
        this.listaFacturas = facturaDAO.consultarFacturasCargadas();
    }

    private static final Logger LOG = Logger.getLogger(ProcesarFacturaCron.class.getSimpleName());

    @Lock(LockType.WRITE)
    public List<Factura> cargarFacturasIniciales() {

        LOG.log(Level.INFO, "=== Creando Hilos procesamiento Facturas Siigo =====");

        hilos = new ConcurrentHashMap<>();
        listaFacturas = new ArrayList<>();
        int tamano = 10;
        List<Factura> carguesIniciales = new ArrayList<>(tamano);
        consultarFacturas();
        HashMap<Long, Long> mapClientes = new HashMap<>();
        int total = 0;
        //System.out.println("Tamano " + cargues.size());
        for (Factura factura : listaFacturas) {
            //System.out.println(cargue.getCargueId());
            Long idcliente = factura.getCliente().getCliId();
            if (!mapClientes.containsKey(idcliente)) {
                mapClientes.put(idcliente, idcliente);
                carguesIniciales.add(factura);
                total++;
            }
            if (total == tamano) {
                break;
            }
        }
        return carguesIniciales;
    }

    @Lock(LockType.WRITE)
    public void marcarFacturaProcesada(Long idFactura, Integer numHilo, Long idCliente) {
        for (Factura factura : listaFacturas) {
            if (Objects.equals(factura.getFacId(), idFactura)) {
                factura.setProcesado(true);
                break;
            }
        }
        hilos.put(idCliente, numHilo);
    }

    @Lock(LockType.WRITE)
    public Factura obtenerSiguienteCargue(int numHilo) {
        for (Factura factura : listaFacturas) {
            if (!factura.isProcesado()) {
                Long idCliente = factura.getCliente().getCliId();
                if (!hilos.containsKey(idCliente)) {
                    hilos.put(idCliente, numHilo);
                    return factura;
                } else {
                    if (numHilo == hilos.get(idCliente)) {
                        return factura;
                    }
                }
            }
        }
        return null;
    }

    @Schedule(second = "0", minute = "*/15", hour = "*", persistent = true)
    //@TransactionAttribute(TransactionAttributeType.NEVER)
    public void procesarCarguesFacturas() {
        
        LOG.log(Level.INFO, "============= TAREAS PROGRAMADAS!!!! ===========");
        LOG.log(Level.INFO, "============= PROCESAR CARGUES FACTURAS --- Inicia ejecucion tarea programada.===========");

        iniciarProceso();

    }

    public void iniciarProceso() {
        List<Factura> carguesIniciales = cargarFacturasIniciales();
        if (carguesIniciales == null || carguesIniciales.isEmpty()) {
            LOG.log(Level.INFO, ">>>>NO HAY FACTURAS PARA PROCESAR --- No se encontraron cargues de para procesar.....");
            return;
        }
        Calendar c = Calendar.getInstance();
        long horaIniMillis = System.currentTimeMillis();
        int horasMax = 4;//Tiempo de espera para el procesamiento de las facturas
        long horasMaxMillis = horasMax * 60 * 60 * 1000L;

 
        int i = 1;
        for (Factura cargue : carguesIniciales) {
            procesarFacturasLogica.procesarCargue(cargue, i, horaIniMillis, horasMaxMillis);
            i++;
        }
    }

}
