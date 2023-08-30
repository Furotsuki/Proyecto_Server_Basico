package Servicios;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    //Declaracion de atributos y metodos
    private static final int puerto = 12345;
    public static ArrayList<String> nombreLista = new ArrayList<>();
    //Muestra la info detallada como la info del dia 
    private static final Logger registro = Logger.getLogger(Servidor.class.getName());
    //Ayuda a gestionar y rendimiento del servidor ya que cree una cantidad predeterminada de hilos
    private static final int tamano_pool_hilo = 10;
    
    
    //Declaracion del main
    public static void main(String[] args) {
        ExecutorService ejecutarServicio = Executors.newFixedThreadPool(tamano_pool_hilo);

        try (ServerSocket servidorSocket = new ServerSocket(puerto)) {
            //Mensaje para informar que el servidor esta en funcionamiento
            registro.info("Servidor iniciado correctamente desde el puerto = " + puerto);
            //Ciclo para comenzar el servicio y la conexion de los clientes con los servidores
            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                ejecutarServicio.execute(new gestionCliente(clienteSocket));
            }
        } catch (IOException e) {
            registro.log(Level.SEVERE, "Error al iniciar el servidor: " + e.getMessage(), e);
        } finally {
            //Realizar un finalizado de manera de manera ordenada las tareas 
            ejecutarServicio.shutdown();
        }
    }

}

