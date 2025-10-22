package org.example.view;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.service.CorridaService;
import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;


public class AvaliacaoView {

    private static final CorridaService cs = new CorridaService();
    private static final MotoristaService ms = new MotoristaService();
    private static final PassageiroService ps = new PassageiroService();

    public static void avaliarMotoristaPeloPassageiro(Corrida corrida) {
        if (corrida.getAvaliacaoMotorista() != 0) {
            return;
        }

        Motorista motorista = ms.buscarPorId(corrida.getMotoristaId());
        if (motorista == null) return;

        ViewUtils.limparConsole();
        System.out.println("--- Avalie sua Viagem ---");
        System.out.println("Motorista: " + motorista.getNome());

        int nota = coletarNota("o motorista " + motorista.getNome());

        corrida.setAvaliacaoMotorista(nota);

        cs.processarAvaliacaoMotorista(corrida, nota);

        System.out.println("\nAvaliação do motorista salva! Pressione ENTER para continuar.");
        ViewUtils.sc.nextLine();
    }


    public static void avaliarPassageiroPeloMotorista(Corrida corrida) {
        if (corrida.getAvaliacaoPassageiro() != 0) {
            return;
        }

        Passageiro passageiro = ps.buscarPorId(corrida.getPassageiroId());
        if (passageiro == null) return;

        ViewUtils.limparConsole();
        System.out.println("--- Avalie o Passageiro ---");
        System.out.println("Passageiro: " + passageiro.getNome());

        int nota = coletarNota("o passageiro " + passageiro.getNome());

        corrida.setAvaliacaoPassageiro(nota);
        cs.processarAvaliacaoPassageiro(corrida, nota);

        System.out.println("\nAvaliação do passageiro salva! Pressione ENTER para voltar ao menu.");
        ViewUtils.sc.nextLine();
    }

    private static int coletarNota(String item) {
        int nota = -1;
        while (nota < 1 || nota > 5) {
            System.out.printf("\nDe 1 a 5, qual a sua avaliação para %s? (5 é excelente): ", item);
            try {
                nota = Integer.parseInt(ViewUtils.sc.nextLine());
                if (nota < 1 || nota > 5) {
                    System.out.println("[ERRO] Nota inválida. Por favor, digite um número entre 1 e 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERRO] Entrada inválida. Por favor, digite um número.");
                nota = -1;
            }
        }
        return nota;
    }
}