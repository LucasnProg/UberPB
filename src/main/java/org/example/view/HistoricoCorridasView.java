package org.example.view;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.service.CorridaService;
import org.example.model.service.MotoristaService;

import java.time.format.DateTimeFormatter;

public class HistoricoCorridasView {

    private static final CorridaService cs = new CorridaService();

    public static void executar(Passageiro passageiro) {
        ViewUtils.limparConsole();
        System.out.println("--- Seu Histórico de Corridas ---");

        if (passageiro.getHistoricoCorridas().isEmpty()) {
            System.out.println("\nVocê ainda não completou nenhuma corrida.");
            System.out.println("Pressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

        for (Corrida corrida : passageiro.getHistoricoCorridas()) {
            System.out.println("\n---------------------------------");
            System.out.println("ID: " + corrida.getId());
            String dataFim = (corrida.getHoraFim() != null) ? corrida.getHoraFim().format(formatter) : "N/A";
            System.out.println("Data: " + dataFim);
            System.out.println("Origem: " + corrida.getOrigem().getDescricao());
            System.out.println("Destino: " + corrida.getDestino().getDescricao());
            System.out.printf("Valor Pago: R$ %.2f\n", corrida.getValor());

            Motorista motorista = cs.getMotoristaById(corrida.getMotoristaId());
            if (motorista != null) {
                System.out.println("Motorista: " + motorista.getNome());
            }
            System.out.println("---------------------------------");
        }

        while (true) {
            System.out.print("\nDigite o ID da corrida para Avaliar (ou 0 para voltar): ");
            String inputId = ViewUtils.sc.nextLine();

            try {
                int idCorrida = Integer.parseInt(inputId);

                if (idCorrida == 0) return;

                Corrida corridaSelecionada = passageiro.getHistoricoCorridas().stream()
                        .filter(c -> c.getId() == idCorrida)
                        .findFirst()
                        .orElse(null);

                if (corridaSelecionada != null) {
                    processarAvaliacao(corridaSelecionada);
                    return;
                } else {
                    System.out.println("\n[ERRO] ID não encontrado no seu histórico.");
                }

            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Digite um número de ID válido.");
            }
        }
    }

    private static void processarAvaliacao(Corrida corrida) {
        while (true) {
            try {
                System.out.print("\nDigite uma nota de 0 a 5 para o motorista: ");
                String notaInput = ViewUtils.sc.nextLine().replace(",", ".");
                double nota = Double.parseDouble(notaInput);

                if (nota < 0 || nota > 5) {
                    System.out.println("\n[ERRO] A nota deve estar entre 0 e 5.");
                } else {
                    MotoristaService.receberAvaliacao(corrida.getMotoristaId(), nota);
                    System.out.println("\nAvaliação enviada com sucesso!");
                    System.out.println("Pressione ENTER para continuar...");
                    ViewUtils.sc.nextLine();
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Formato de nota inválido. Use apenas números (ex: 4.5).");
            }
        }
    }
}