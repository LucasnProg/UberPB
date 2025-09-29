package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.CorridaService;
import org.example.model.service.LocalizacaoService;
import org.example.model.service.MotoristaService;
import org.example.model.service.SimuladorViagem;

import java.util.List;

/**
 * View focada na interface do Motorista logado.
 */
public class CorridaView {

    private static final CorridaService cs = new CorridaService();
    private static final MotoristaService ms = new MotoristaService();

    public static void menuMotorista(Motorista motorista) {
        while (true) {
            Motorista motoristaAtualizado = ms.buscarPorId(motorista.getId());
            if (motoristaAtualizado == null) {
                System.out.println("[ERRO] Não foi possível encontrar os dados do motorista.");
                return;
            }

            ViewUtils.limparConsole();
            System.out.println("--- Menu do Motorista ---");
            System.out.println("Olá, " + motoristaAtualizado.getNome() + "! Status: " + motoristaAtualizado.getStatus());
            if (motoristaAtualizado.getLocalizacao() != null) {
                System.out.println("Localização Atual: " + motoristaAtualizado.getLocalizacao().getDescricao());
            }

            // Verifica se o motorista já tem uma corrida em andamento
            Corrida corridaAtiva = cs.buscarCorridaAtivaPorMotorista(motoristaAtualizado);
            if (corridaAtiva != null) {
                gerenciarCorridaAtiva(corridaAtiva);
                continue;
            }

            System.out.println("\n1 - Ver Corridas Notificadas (" + motoristaAtualizado.getCorridasNotificadas().size() + ")");
            System.out.println("2 - Simular / Mudar Localização");
            System.out.println("0 - Fazer Logout");
            System.out.print("\nEscolha uma opção: ");

            String opcao = ViewUtils.sc.nextLine();
            switch (opcao) {
                case "1":
                    verCorridasNotificadas(motoristaAtualizado);
                    break;
                case "2":
                    simularNovaLocalizacao(motoristaAtualizado);
                    break;
                case "0":
                    System.out.println("\nFazendo logout...");
                    return;
            }
        }
    }

    private static void verCorridasNotificadas(Motorista motorista) {
        ViewUtils.limparConsole();
        System.out.println("--- Corridas Notificadas ---");
        List<Corrida> notificadas = motorista.getCorridasNotificadas();

        if (notificadas.isEmpty()) {
            System.out.println("Nenhuma nova corrida no momento.");
            System.out.println("\nPressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        for (Corrida c : notificadas) {
            String infoPagamento = " (" + c.getFormaPagamento().getDescricao() + ")";
            System.out.printf("ID: %d | Origem: %s | Destino: %s | Valor: R$ %.2f%s\n",
                    c.getId(), c.getOrigem().getDescricao(), c.getDestino().getDescricao(), c.getValor(), infoPagamento);
        }

        System.out.print("\nDigite o ID da corrida para interagir (ou 0 para voltar): ");
        try {
            int idCorrida = Integer.parseInt(ViewUtils.sc.nextLine());
            if (idCorrida == 0) return;

            Corrida corridaSelecionada = notificadas.stream()
                    .filter(c -> c.getId() == idCorrida).findFirst().orElse(null);

            if (corridaSelecionada != null) {
                interagirComCorrida(motorista, corridaSelecionada);
            } else {
                System.out.println("\n[ERRO] ID de corrida inválido. Pressione ENTER.");
                ViewUtils.sc.nextLine();
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] Entrada inválida. Pressione ENTER.");
            ViewUtils.sc.nextLine();
        }
    }

    private static void interagirComCorrida(Motorista motorista, Corrida corrida) {
        ViewUtils.limparConsole();
        System.out.println("--- Detalhes da Corrida ID: " + corrida.getId() + " ---");
        Passageiro passageiro = cs.getPassageiroById(corrida.getPassageiroId());

        System.out.println("Passageiro: " + (passageiro != null ? passageiro.getNome() : "N/A"));
        System.out.println("Origem: " + corrida.getOrigem().getDescricao());
        System.out.println("Destino: " + corrida.getDestino().getDescricao());
        System.out.println("Categoria: " + corrida.getCategoriaVeiculo().getNome());
        System.out.printf("Valor Estimado: R$ %.2f\n", corrida.getValor());
        System.out.println("Forma de Pagamento: " + corrida.getFormaPagamento().getDescricao());

        System.out.println("\n1 - Aceitar e Iniciar Corrida");
        System.out.println("2 - Negar Corrida");
        System.out.println("0 - Voltar");
        System.out.print("\nEscolha uma opção: ");

        switch (ViewUtils.sc.nextLine()) {
            case "1":
                boolean sucesso = cs.aceitarCorrida(motorista, corrida);
                if (sucesso) {
                    System.out.println("\nCorrida aceita e iniciada!");
                    System.out.println("Forma de Pagamento Confirmada: " + corrida.getFormaPagamento().getDescricao());
                    System.out.println("O mapa da viagem será aberto.");
                }
                System.out.println("Pressione ENTER para continuar.");
                ViewUtils.sc.nextLine();
                break;
            case "2":
                ms.negarCorrida(motorista, corrida);
                System.out.println("\nCorrida negada.");
                System.out.println("Pressione ENTER para continuar.");
                ViewUtils.sc.nextLine();
                break;
        }
    }

    private static void gerenciarCorridaAtiva(Corrida corrida) {
        ViewUtils.limparConsole();
        System.out.println("--- Gerenciando Corrida Atual ---");
        Passageiro passageiro = cs.getPassageiroById(corrida.getPassageiroId());

        System.out.println("Passageiro: " + (passageiro != null ? passageiro.getNome() : "N/A"));
        System.out.println("Status Atual: " + corrida.getStatus());

        // Como a corrida já começa EM_CURSO, vamos direto para a simulação
        if (corrida.getStatus() == StatusCorrida.EM_CURSO) {
            SimuladorViagem.prepararSimulacao(corrida);
            MapaView.abrirMapa();
            SimuladorViagem.simular(corrida);

            cs.finalizarCorrida(corrida); // Finaliza ao fim da simulação
            System.out.println("\nPressione ENTER para voltar ao menu.");
            ViewUtils.sc.nextLine();
        }
    }

    private static void simularNovaLocalizacao(Motorista motorista) {
        ViewUtils.limparConsole();
        System.out.println("--- Simular Nova Localização ---");

        LocalizacaoService ls = new LocalizacaoService();
        List<Localizacao> locais = ls.carregarLocais();

        if (locais == null || locais.isEmpty()) {
            System.out.println("Nenhum local cadastrado para simulação. Pressione ENTER.");
            ViewUtils.sc.nextLine();
            return;
        }

        System.out.println("Escolha para onde você quer 'ir':");
        int i = 1;
        for (Localizacao local : locais) {
            System.out.println(i++ + " - " + local.getDescricao());
        }
        System.out.println("0 - Voltar");
        System.out.print("\nEscolha uma opção: ");

        try {
            int escolha = Integer.parseInt(ViewUtils.sc.nextLine());
            if (escolha > 0 && escolha <= locais.size()) {
                Localizacao novaLocalizacao = locais.get(escolha - 1);
                motorista.setLocalizacao(novaLocalizacao);
                ms.atualizar(motorista);
                System.out.println("\nLocalização atualizada para: " + novaLocalizacao.getDescricao());
            }
        } catch (NumberFormatException e) {
            System.out.println("\n[ERRO] Opção inválida.");
        }
        System.out.println("Pressione ENTER para voltar ao menu.");
        ViewUtils.sc.nextLine();
    }

    public static CategoriaVeiculo selecionarCategoria() {
        while (true) {
            System.out.println("\n--- Selecione a Categoria do Veículo ---");
            int i = 1;
            for (CategoriaVeiculo categoria : CategoriaVeiculo.values()) {
                System.out.printf("%d - %-12s (%s)\n", i++, categoria.getNome(), categoria.getDescricao());
            }

            System.out.print("\nEscolha uma opção (ou digite '0' para voltar): ");
            try {
                String input = ViewUtils.sc.nextLine();
                int cat = Integer.parseInt(input);

                if (cat == 0) return null;

                if (cat > 0 && cat <= CategoriaVeiculo.values().length) {
                    return CategoriaVeiculo.values()[cat - 1];
                } else {
                    System.out.println("\n[ERRO] Opção inválida. Pressione ENTER.");
                    ViewUtils.sc.nextLine();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Por favor, digite um número. Pressione ENTER.");
                ViewUtils.sc.nextLine();
            }
        }
    }
}