package org.example.view;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.service.CorridaService;

/**
 * View responsável por mostrar ao passageiro suas corridas solicitadas
 * que ainda não foram finalizadas.
 */
public class AcompanharCorridasView {

    private static final CorridaService cs = new CorridaService();

    public static void executar(Passageiro passageiro) {
        ViewUtils.limparConsole();
        System.out.println("--- Suas Corridas Solicitadas ---");

        if (passageiro.getCorridasPendentes().isEmpty()) {
            System.out.println("\nVocê não possui corridas pendentes no momento.");
        } else {
            for (Corrida corrida : passageiro.getCorridasPendentes()) {
                Corrida corridaAtualizada = cs.buscarCorridaPorId(corrida.getId());
                if (corridaAtualizada == null) continue;

                System.out.println("\n---------------------------------");
                System.out.println("ID da Corrida: " + corridaAtualizada.getId());
                System.out.println("Origem: " + corridaAtualizada.getOrigem().getDescricao());
                System.out.println("Destino: " + corridaAtualizada.getDestino().getDescricao());
                System.out.printf("Valor Estimado: R$ %.2f\n", corridaAtualizada.getValor());

                String statusFormatado = formatarStatus(corridaAtualizada);
                System.out.println("Status: " + statusFormatado);

                if (corridaAtualizada.getMotoristaId() > 0) {
                    Motorista motorista = cs.getMotoristaById(corridaAtualizada.getMotoristaId());
                    if (motorista != null) {
                        System.out.println("Motorista: " + motorista.getNome());
                    }
                }
                System.out.println("---------------------------------");
            }
        }

        System.out.println("\nPressione ENTER para voltar ao menu.");
        ViewUtils.sc.nextLine();
    }

    /**
     * Converte o Enum StatusCorrida para uma string mais clara para o usuário final.
     */
    private static String formatarStatus(Corrida corrida) {
        switch (corrida.getStatus()) {
            case SOLICITADA:
                return "Aguardando um motorista aceitar.";
            case ACEITA:
                return "Motorista a caminho!";
            case EM_CURSO:
                return "Em Viagem. Boa viagem!";
            case FINALIZADA:
                return "Viagem Concluída.";
            case CANCELADA:
                return "Viagem Cancelada.";
            default:
                return "Status desconhecido.";
        }
    }
}