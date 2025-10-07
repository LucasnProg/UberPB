package org.example.view;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.service.CorridaService;

import java.time.format.DateTimeFormatter;

/**
 * View responsável por exibir o histórico de corridas finalizadas de um passageiro.
 */
public class HistoricoCorridasView {

    private static final CorridaService cs = new CorridaService();

    public static void executar(Passageiro passageiro) {
        ViewUtils.limparConsole();
        System.out.println("--- Seu Histórico de Corridas ---");

        if (passageiro.getHistoricoCorridas().isEmpty()) {
            System.out.println("\nVocê ainda não completou nenhuma corrida.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

            for (Corrida corrida : passageiro.getHistoricoCorridas()) {
                System.out.println("\n---------------------------------");
                System.out.println("Data: " + corrida.getHoraFim().format(formatter));
                System.out.println("Origem: " + corrida.getOrigem().getDescricao());
                System.out.println("Destino: " + corrida.getDestino().getDescricao());
                System.out.printf("Valor Pago: R$ %.2f\n", corrida.getValor());
                System.out.println("Forma de pagamento: " + corrida.getFormaPagamento().getDescricao());

                Motorista motorista = cs.getMotoristaById(corrida.getMotoristaId());
                if (motorista != null) {
                    System.out.println("Motorista: " + motorista.getNome());
                }
                System.out.println("---------------------------------");
            }
        }

        System.out.println("\nPressione ENTER para voltar ao menu.");
        ViewUtils.sc.nextLine();
    }
}