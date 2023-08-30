package Clientecitos;

import java.util.LinkedList;
import java.util.Queue;

public class ClientesManager {
    //Es un metodo para que los datos se impriman desde la cola de la lista y no desde la cabeza
    private Queue<ClienteInfo> colaCliente = new LinkedList<>();

    public synchronized void agregarCliente(String nombre, String mensaje) {
        colaCliente.offer(new ClienteInfo(nombre, mensaje));
    }
    //Remueve los clientes o la informacion que este vacia
    public synchronized ClienteInfo obtenerCliente() {
        return colaCliente.poll();
    }
    //Revisa si la lista esta vacia
    public synchronized boolean hayClientes() {
        return !colaCliente.isEmpty();
    }
}
