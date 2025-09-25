package org.example;

import org.example.view.View;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        View.iniciar();
        //cli.iniciar();
        //abrirMapa();
    }

    /*public static void abrirMapa() {
        try {
            File htmlFile = new File("src/main/java/org/example/view/Mapa/Mapa.html");
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch (IOException e) {
            System.out.println("Não foi possível abrir o mapa.");
            e.printStackTrace();
        }
    }*/
}
