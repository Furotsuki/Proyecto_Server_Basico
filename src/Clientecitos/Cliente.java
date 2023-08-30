package Clientecitos;

import javax.swing.*;

public class Cliente {
    /*Manejo del main de la clase Cliente en donde se invoca la clase Ventana que compone la logica 
    y parte grafica del archivo*/
    public static void main(String[] args) {
        // Crear y mostrar la GUI
        Ventana ventana = new Ventana();
        ventana.crear_mostrar_GUI();

    }

}
