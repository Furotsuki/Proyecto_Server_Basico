package Servicios;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class hiloServidor extends Thread {

    private static final int puerto = 12345;
    //Muestra informacion especifica del servidor
    private static final Logger registro = Logger.getLogger(hiloServidor.class.getName());
    //Genera una cantidad de hilos especificos para redecir la carga y optimizar el servidor
    private static final int tamano_pool_hilo = 10;

    @Override
    public void run() {
        ExecutorService ejecutarServicio = Executors.newFixedThreadPool(tamano_pool_hilo);

        try (ServerSocket servidorSocket = new ServerSocket(puerto)) {
            while (true) {
                Socket clienteSocket = servidorSocket.accept();
                ejecutarServicio.execute(new gestionCliente(clienteSocket));
            }
        } catch (IOException e) {
            registro.log(Level.SEVERE, "Error al iniciar el servidor: " + e.getMessage(), e);
        } finally {
            ejecutarServicio.shutdown();
        }
    }
}
