package org.example.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

/**
 * View utilitária para interagir com o mapa em HTML.
 */
public class MapaView {

    /**
     * Abre o arquivo Mapa.html no navegador padrão do sistema.
     */
    public static void abrirMapa() {
        File htmlFile = new File("src/main/java/org/example/view/Mapa/Mapa.html");
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(htmlFile.toURI());
                System.out.println("\nAbrindo mapa de acompanhamento no seu navegador...");
            } catch (IOException e) {
                System.out.println("\n[ERRO] Não foi possível abrir o mapa no navegador automaticamente.");
            }
        } else {
            System.out.println("\n[AVISO] Abertura de navegador não é suportada neste ambiente.");
        }
    }
}