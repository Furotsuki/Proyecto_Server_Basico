package Servicios;

import java.io.*;
import java.net.*;
import java.util.Collections;
import Servicios.Servidor;

class gestionCliente extends Thread {
    
    private Socket clienteSocket;

    public gestionCliente(Socket conectar) {
        this.clienteSocket = conectar;
    }
    

    @Override
    /*Desde este hilo que se genera se hace el subproceso para poder enviar desde varios clientes los mensajes y que se
      impriman de manera correcta y sin ningun error de desbordamiento de capacidad del servidor
    */
    public void run() {
        try (BufferedReader entra = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
             PrintWriter sale = new PrintWriter(clienteSocket.getOutputStream(), true)) {

            String nombre = entra.readLine();
            String mensaje = entra.readLine();

            Servidor.nombreLista.add(nombre + " dijo: " + mensaje);
            Collections.sort(Servidor.nombreLista);

            StringBuilder responder = new StringBuilder();
            for (String n : Servidor.nombreLista) {
                responder.append("Mensaje de usuario\n");
                responder.append(n).append("\n");
            }

            responder.append("Mensaje actual del usuario\n");
            responder.append("Nombre del usuario: ").append(nombre).append("\n");
            responder.append("Dijo: ").append(mensaje).append("\n");

            sale.println(responder.toString());

        } catch (IOException e) {
            System.err.println("Error al manejar el cliente: " + e.getMessage());
        }
    }

}
