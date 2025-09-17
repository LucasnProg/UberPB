package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.CorridaService;
import org.example.model.service.LocalizacaoService;
import org.example.model.service.MotoristaService;

import java.util.List;

public class CorridaView {

    private static final CorridaService cs = new CorridaService();
    private static final MotoristaService ms = new MotoristaService();


    public static void menuCorridaPassageiro(Passageiro passageiro) {
        ViewUtils.limparConsole();
        System.out.println("--- Solicitar Corrida ---");
        System.out.println("Olá, " + passageiro.getNome() + "!");

        List<Localizacao> locais = LocalizacaoService.carregarLocais();
        if (locais == null || locais.isEmpty()) { /* ... */ return; }

        Localizacao origem = selecionarLocal("origem", locais);
        if (origem == null) return;

        Localizacao destino = selecionarLocal("destino", locais);
        if (destino == null) return;

        if (origem.equals(destino)) { /* ... */ return; }

        CategoriaVeiculo categoria = selecionarCategoria();
        if (categoria == null) return;

        System.out.println("\nCalculando estimativa...");
        double precoEstimado = cs.calcularPrecoEstimado(origem, destino, categoria);

        System.out.printf("Preço Estimado: R$ %.2f\n", precoEstimado);

        System.out.print("\nDeseja confirmar a solicitação? (S/N): ");
        if (ViewUtils.sc.nextLine().equalsIgnoreCase("S")) {
            System.out.println("\nProcurando motorista...");
            Corrida corridaSolicitada = cs.solicitarCorrida(passageiro, origem, destino, categoria);

            if (corridaSolicitada != null) {
                acompanharCorridaPassageiro(corridaSolicitada);
            } else {
                System.out.println("\n[INFO] Não há motoristas disponíveis no momento.");
                ViewUtils.sc.nextLine();
            }
        } else {
            System.out.println("\nSolicitação cancelada.");
            ViewUtils.sc.nextLine();
        }
    }

    private static void acompanharCorridaPassageiro(Corrida corrida) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("--- Acompanhando sua Viagem ---");

            Corrida corridaAtualizada = cs.buscarCorridaPorId(corrida.getId());
            if (corridaAtualizada == null) {
                System.out.println("Corrida não encontrada.");
                break;
            }

            // --- ALTERAÇÃO: Buscando motorista pelo ID para exibir o nome ---
            Motorista motorista = ms.getMotoristaByID(corridaAtualizada.getMotoristaId());

            System.out.println("Origem: " + corridaAtualizada.getOrigem().getDescricao());
            System.out.println("Destino: " + corridaAtualizada.getDestino().getDescricao());
            System.out.println("Status: " + corridaAtualizada.getStatus());

            if (motorista != null) {
                System.out.println("Motorista: " + motorista.getNome());
            }

            if (corridaAtualizada.getStatus() == StatusCorrida.FINALIZADA || corridaAtualizada.getStatus() == StatusCorrida.CANCELADA) {
                break;
            }

            try { Thread.sleep(5000); } catch (InterruptedException e) {}
        }
        System.out.println("\nViagem concluída! Pressione ENTER para voltar ao menu.");
        ViewUtils.sc.nextLine();
    }

    // --- ALTERAÇÃO: Retorna o enum CategoriaVeiculo ---
    private static CategoriaVeiculo selecionarCategoria() {
        while (true) {
            System.out.println("\nSelecione a categoria de veículo:");
            System.out.println("1 - UberX | 2 - Comfort | 3 - Black | 4 - Bag | 5 - XL");
            System.out.print("Escolha uma opção (ou digite '0' para voltar): ");
            try {
                int cat = Integer.parseInt(ViewUtils.sc.nextLine());
                if (cat == 0) return null;
                if (cat >= 1 && cat <= 5) {
                    return cs.categoriaPorOpcao(cat);
                }
                System.out.println("\n[ERRO] Opção inválida.");
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Por favor, digite um número.");
            }
        }
    }

    private static Localizacao selecionarLocal(String tipo, List<Localizacao> locais) {
        // Este método não precisa de alterações, pois já usa getDescricao()
        // ...
        return null;
    }

    public static void menuMotorista(Motorista motorista) {
        // O menu do motorista também precisará ser adaptado para usar os novos status e IDs...
    }

    private static void gerenciarCorridaMotorista(Corrida corrida) {
        // Este método também precisará de adaptação, buscando o passageiro pelo ID para exibir o nome
        // Passageiro passageiro = cs.getPassageiroById(corrida.getPassageiroId());
    }
}