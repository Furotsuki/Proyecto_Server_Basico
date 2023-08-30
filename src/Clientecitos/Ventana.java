package Clientecitos;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import Servicios.hiloServidor;

public class Ventana {

    private static final String HOST = "localhost";
    private static final int puerto = 12345;
    //Crea el area de texto en donde se imprime toda la infomracion suministrada por el Cliente
    private static JTextArea areaTexto;
    private static ArrayList<String> nombreLista = new ArrayList<>();
    //Trae la clase cliente director que manipula la lista de clientes para cambiar el sentido de como llegan los clientes
    private static ClientesManager cliente_director = new ClientesManager();

    //Genera la parte grafica del cliente
    public void crear_mostrar_GUI() {
        JFrame marco = new JFrame("Client GUI");
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(500, 400);

        JPanel costado = new JPanel();
        costado.setSize(400, 100);

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);
        JScrollPane panel_despla = new JScrollPane(areaTexto);
        panel_despla.setSize(400, 300);

        JPanel panel_entrada = new JPanel(new GridLayout(2, 2, 10, 5));

        JLabel tabla_nombre = new JLabel("Nombre:");
        JLabel tabla_mensaje = new JLabel("Mensaje:");
        JTextField nombre_usuario = new JTextField(20);
        JTextField Mensaje = new JTextField(20);

        panel_entrada.add(tabla_nombre);
        panel_entrada.add(nombre_usuario);
        panel_entrada.add(tabla_mensaje);
        panel_entrada.add(Mensaje);

        JButton botonEnviar = new JButton("Enviar Mensaje");
        JButton botonSalir = new JButton("Salir");
        botonSalir.setSize(40, 30);
        botonEnviar.addActionListener(e -> {
            String nombre = nombre_usuario.getText().trim();
            String mensaje_envia = Mensaje.getText().trim();
            if (!nombre.isEmpty()) {
                enviar_nombre_servidor(nombre, mensaje_envia);
            }
        });
        
        //Boton de salida
        botonSalir.addActionListener(e -> System.exit(0));
        
        //Panel derecho en donde se encuentran los botones
        JPanel envio = new JPanel();
        envio.add(botonEnviar);
        envio.add(botonSalir);

        marco.setLayout(new BorderLayout());
        marco.add(panel_entrada, BorderLayout.NORTH);
        marco.add(panel_despla, BorderLayout.CENTER);
        marco.add(envio, BorderLayout.EAST);
        marco.setVisible(true);
    }
    
    /*Esta clase se genera para en el caso dado de que el Servidor se caiga
    el cliente actual en enviar el mensaje se convierta en servidor y se genere un nuevo Cliente
    que "remplaze" al anterior
    */
    public void comenzar_nuevo_cliente() {
        SwingUtilities.invokeLater(this::crear_mostrar_GUI);
    }

    private void enviar_nombre_servidor(String nombre, String mensaje) {
        try (Socket conectar = new Socket(HOST, puerto);
             /*El PrintWriter es para que se puedan escrbiri flujos de datos de varios tipos de manera legible*/
             PrintWriter salida_mensaje = new PrintWriter(conectar.getOutputStream(), true);
             PrintWriter salir_mensaje = new PrintWriter(conectar.getOutputStream(), true);
             BufferedReader entra = new BufferedReader(new InputStreamReader(conectar.getInputStream()))) {

            salida_mensaje.println(nombre);
            salir_mensaje.println(mensaje);

            nombreLista.clear();
            String recibirNombre;
            //Verifica hasta donde va la lisya o nombre para luego poder imprimir la lista completa
            while ((recibirNombre = entra.readLine()) != null && !recibirNombre.isEmpty()) {
                nombreLista.add(recibirNombre);
            }
            actualizar_area_texto();
            
            //Cambia lista de sentido para que vaya de arriba hacia abaja entre clientes
            while (entra.ready()) {
                String recibirNombreCliente = entra.readLine();
                String recibirNombreMensaje = entra.readLine();
                cliente_director.agregarCliente(recibirNombreCliente, recibirNombreMensaje);
            }

            while (cliente_director.hayClientes()) {
                ClienteInfo clienteInfo = cliente_director.obtenerCliente();
                anadir_areaTexto(clienteInfo.getNombre() + ": " + clienteInfo.getMensaje());
            }

        } catch (IOException e) {
            //En caso tal de que el servidor cae, un cliente toma el papel de servidor y genera otro cliene ademas de ello
            JOptionPane.showMessageDialog(null, "Error al conectar con el servidor. Intentando convertirse en servidor...");
            hiloServidor hiloServidor = new hiloServidor();
            hiloServidor.start();
            comenzar_nuevo_cliente();
        }
    }
    //El metodo es para  que se actualize el area de texto en donde se imprime la informacion
    private static void actualizar_area_texto() {
        StringBuilder construir = new StringBuilder();
        for (String nombre : nombreLista) {
            construir.append(nombre).append("\n");
        }
        areaTexto.setText(construir.toString());
    }
    //Añade lo que el usuario escribe en las cajas de texto
    private static void anadir_areaTexto(String texto) {
        SwingUtilities.invokeLater(() -> areaTexto.append(texto + "\n"));
    }
}