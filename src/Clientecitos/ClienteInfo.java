package Clientecitos;

public class ClienteInfo {

    private String nombre;
    private String mensaje;

    public ClienteInfo(String nombre, String mensaje) {
        this.nombre = nombre;
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMensaje() {
        return mensaje;
    }
}
