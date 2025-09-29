package org.example.model.service;

import org.example.model.entity.Corrida;
import org.example.model.entity.Localizacao;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe responsável por simular o progresso de uma viagem.
 */
public class SimuladorViagem {

    private static final int NUMERO_DE_PASSOS = 20; // A viagem será dividida em 20 etapas
    private static final int INTERVALO_MS = 2000;   // Intervalo de 2 segundos entre cada etapa

    /**
     * Executa a simulação de uma corrida, atualizando um arquivo JS para o mapa.
     * @param corrida O objeto Corrida a ser simulado.
     */
    public static void simular(Corrida corrida) {
        Localizacao origem = corrida.getOrigem();
        Localizacao destino = corrida.getDestino();

        System.out.println("\n--- Iniciando simulação da viagem no mapa ---");
        System.out.println("Por favor, verifique a janela do navegador que foi aberta.");

        for (int i = 0; i <= NUMERO_DE_PASSOS; i++) {
            // Calcula a posição intermediária usando interpolação linear
            double fator = (double) i / NUMERO_DE_PASSOS;
            double latAtual = origem.getLatitude() + fator * (destino.getLatitude() - origem.getLatitude());
            double lonAtual = origem.getLongitude() + fator * (destino.getLongitude() - origem.getLongitude());

            Localizacao posAtual = new Localizacao(latAtual, lonAtual);

            // Escreve a posição atual no arquivo JS
            escreverArquivoJS(origem, destino, posAtual);

            // Imprime o progresso no console
            System.out.printf("Simulação: Passo %d de %d... Posição: (%.4f, %.4f)\n", i, NUMERO_DE_PASSOS, latAtual, lonAtual);

            try {
                Thread.sleep(INTERVALO_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Simulação interrompida.");
            }
        }
        System.out.println("--- Simulação da viagem finalizada ---");
    }

    /**
     * Escreve/sobrescreve o arquivo dados_viagem.js com as coordenadas atuais.
     */
    private static void escreverArquivoJS(Localizacao origem, Localizacao destino, Localizacao posCarro) {
        // O caminho aponta para a mesma pasta do Mapa.html
        String caminhoArquivo = "src/main/java/org/example/view/Mapa/dados_viagem.js";

        String conteudo = String.format(
                "var origem = { lat: %f, lon: %f, desc: \"%s\" };\n" +
                        "var destino = { lat: %f, lon: %f, desc: \"%s\" };\n" +
                        "var carro_pos = { lat: %f, lon: %f };",
                origem.getLatitude(), origem.getLongitude(), origem.getDescricao(),
                destino.getLatitude(), destino.getLongitude(), destino.getDescricao(),
                posCarro.getLatitude(), posCarro.getLongitude()
        ).replace(",", "."); // Garante que os decimais usem ponto

        try (FileWriter writer = new FileWriter(caminhoArquivo)) {
            writer.write(conteudo);
        } catch (IOException e) {
            System.err.println("Erro ao escrever arquivo de simulação: " + e.getMessage());
        }
    }
}