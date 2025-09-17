package org.example.view;

import java.io.IOException;
import java.util.Scanner;

public class ViewUtils {

    public static final Scanner sc = new Scanner(System.in);

    /**
     * Limpa a tela do console. Compatível com Windows, Linux e macOS.
     */
    public static void limparConsole() {
        try {
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}