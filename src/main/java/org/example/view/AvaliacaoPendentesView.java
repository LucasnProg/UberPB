package org.example.view;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.entity.StatusCorrida;
import org.example.model.service.CorridaService;

import java.util.List;
import java.util.stream.Collectors;

public class AvaliacaoPendentesView {

    private static final CorridaService cs = new CorridaService();
    public static void avaliarComoPassageiro(Passageiro passageiro) {
        List<Corrida> pendentes = cs.buscarCorridasFinalizadas(passageiro.getId()).stream()
                .filter(c -> !c.isPassageiroAvaliou())
                .collect(Collectors.toList());

        if (gerenciarLista(pendentes, "Passageiro")) {
            for (Corrida corrida : pendentes) {
                AvaliacaoView.avaliarMotoristaPeloPassageiro(corrida);
            }
        }
    }
    public static void avaliarComoMotorista(Motorista motorista) {
        List<Corrida> pendentes = cs.buscarCorridasFinalizadas(motorista.getId()).stream()
                .filter(c -> !c.isMotoristaAvaliou())
                .collect(Collectors.toList());

        if (gerenciarLista(pendentes, "Motorista")) {
            for (Corrida corrida : pendentes) {
                AvaliacaoView.avaliarPassageiroPeloMotorista(corrida);
            }
        }
    }

    private static boolean gerenciarLista(List<Corrida> pendentes, String tipoUsuario) {
        ViewUtils.limparConsole();
        System.out.println("--- " + tipoUsuario + ": Avaliações Pendentes ---");

        if (pendentes.isEmpty()) {
            System.out.println("Nenhuma corrida finalizada aguardando sua avaliação.");
            System.out.println("\nPressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return false;
        }

        System.out.println("\nVocê tem " + pendentes.size() + " corridas para avaliar. Avaliando agora...");
        return true;
    }
}