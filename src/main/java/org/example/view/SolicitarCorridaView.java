// Arquivo: view/SolicitarCorridaView.java
package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.CorridaService;
import org.example.model.service.LocalizacaoService;
import org.example.model.service.PassageiroService;
import org.example.model.service.SimuladorViagem;

import java.util.List;

/**
 * View dedicada ao fluxo de solicitação de uma nova corrida pelo passageiro.
 */
public class SolicitarCorridaView {

    private static final CorridaService cs = new CorridaService();
    private static final LocalizacaoService ls = new LocalizacaoService();

    /**
     * Executa o passo a passo para a solicitação de uma nova corrida.
     */
    public static void executar(Passageiro passageiro) {
        ViewUtils.limparConsole();
        System.out.println("--- Solicitar Nova Corrida ---");

        List<Localizacao> locais = ls.carregarLocais();
        if (locais == null) {
            System.out.println("\n[ERRO] Não foi possível carregar os locais. Pressione ENTER.");
            ViewUtils.sc.nextLine();
            return;
        }

        Localizacao origem = selecionarLocal("origem", locais);
        if (origem == null) return;

        locais = ls.carregarLocais();
        Localizacao destino = selecionarLocal("destino", locais);
        if (destino == null) return;

        if (origem.equals(destino)) {
            System.out.println("\n[ERRO] A origem e o destino não podem ser iguais. Pressione ENTER.");
            ViewUtils.sc.nextLine();
            return;
        }

        CategoriaVeiculo categoria = CorridaView.selecionarCategoria();
        if (categoria == null) return;

        System.out.println("\nCalculando estimativa e verificando disponibilidade de motoristas...");
        double estimativaValor = cs.calcularPrecoEstimado(origem, destino, categoria);
        System.out.printf("Preço Estimado: R$ %.2f\n", estimativaValor);

        if (!cs.verificarMotoristasDisponiveis(origem, categoria)) {
            System.out.println("\n[INFO] Desculpe, não há motoristas para esta categoria na sua região no momento.");
            System.out.println("Pressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        System.out.print("\nDeseja prosseguir para o pagamento e solicitar a corrida? (S/N): ");
        if (ViewUtils.sc.nextLine().equalsIgnoreCase("S")) {
            Corrida corridaTemporaria = new Corrida();
            corridaTemporaria.setValor(estimativaValor);

            FormaPagamento formaPagamento = PagamentoView.executar(corridaTemporaria);

            if (formaPagamento != null) {
                System.out.println("\nBuscando motoristas na sua região...");
                Corrida corridaSolicitada = cs.solicitarCorrida(passageiro, origem, destino, categoria, formaPagamento);
                if (corridaSolicitada != null) {
                    acompanharCorridaPassageiro(corridaSolicitada);
                }
            } else {
                System.out.println("\nSolicitação cancelada.");
            }
        } else {
            System.out.println("\nSolicitação cancelada.");
        }

        System.out.println("\nPressione ENTER para voltar ao menu.");
        ViewUtils.sc.nextLine();
    }

    private static Localizacao selecionarLocal(String tipo, List<Localizacao> locais) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("--- Escolha o local de " + tipo + " ---");

            int i = 1;
            for (Localizacao local : locais) {
                System.out.println(i++ + " - " + local.getDescricao());
            }

            System.out.println("\n" + i + " - Adicionar Nova Localização");
            System.out.println("0 - Voltar/Cancelar");
            System.out.print("\nEscolha uma opção: ");

            String input = ViewUtils.sc.nextLine();

            try {
                int escolha = Integer.parseInt(input);
                if (escolha == 0) return null;

                if (escolha == i) {
                    return cadastrarNovoLocal();
                }

                if (escolha > 0 && escolha <= locais.size()) {
                    return locais.get(escolha - 1);
                } else {
                    System.out.println("\n[ERRO] Opção inválida. Pressione ENTER para tentar novamente.");
                    ViewUtils.sc.nextLine();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Pressione ENTER para tentar novamente.");
                ViewUtils.sc.nextLine();
            }
        }
    }

    private static Localizacao cadastrarNovoLocal() {
        ViewUtils.limparConsole();
        System.out.println("--- Adicionar Nova Localização ---");
        System.out.println("(Digite 'voltar' a qualquer momento para cancelar)");

        System.out.print("Dê um nome para o local (ex: Casa, Trabalho): ");
        String descricao = ViewUtils.sc.nextLine();
        if (descricao.equalsIgnoreCase("voltar") || descricao.trim().isEmpty()) return null;

        double latitude, longitude;

        while (true) {
            System.out.print("Digite a Latitude (ex: -7.2192): ");
            String latInput = ViewUtils.sc.nextLine();
            if (latInput.equalsIgnoreCase("voltar")) return null;
            try {
                latitude = Double.parseDouble(latInput.replace(",", "."));
                break;
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Latitude inválida. Use apenas números e ponto/vírgula decimal.");
            }
        }

        while (true) {
            System.out.print("Digite a Longitude (ex: -35.9034): ");
            String lonInput = ViewUtils.sc.nextLine();
            if (lonInput.equalsIgnoreCase("voltar")) return null;
            try {
                longitude = Double.parseDouble(lonInput.replace(",", "."));
                break;
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Longitude inválida. Use apenas números e ponto/vírgula decimal.");
            }
        }

        Localizacao novoLocal = new Localizacao(latitude, longitude);
        novoLocal.setDescricao(descricao);
        ls.adicionarNovoLocal(novoLocal);
        System.out.println("\nLocal '" + descricao + "' adicionado com sucesso!");
        System.out.println("Pressione ENTER para continuar...");
        ViewUtils.sc.nextLine();
        return novoLocal;
    }

    public static void exibirDetalhesPagamentoAoAceite(Corrida corrida, Motorista motorista) {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento da Corrida (Motorista Encontrado) ---");

        if (corrida.getFormaPagamento() == FormaPagamento.PIX) {
            String chavePix = (motorista != null && motorista.getChavePix() != null && !motorista.getChavePix().isEmpty())
                    ? motorista.getChavePix()
                    : "Chave PIX não cadastrada. (Simulação).";

            System.out.println("Pagamento: PIX (Ao Motorista)");
            System.out.printf("Valor: R$ %.2f\n", corrida.getValor());
            System.out.println("----------------------------------------");
            System.out.println("CHAVE PIX DO MOTORISTA:");
            System.out.println(chavePix);
            System.out.println("[QR CODE FICTÍCIO]");
            System.out.println("----------------------------------------");

        } else if (corrida.getFormaPagamento() == FormaPagamento.DINHEIRO) {
            String trocoInfo = corrida.isPrecisaTroco() ? "PASSAGEIRO PRECISA DE TROCO!" : "Não precisa de troco.";

            System.out.println("Pagamento: DINHEIRO (Ao Motorista)");
            System.out.printf("Valor: R$ %.2f\n", corrida.getValor());
            System.out.println("----------------------------------------");
            System.out.println("OBSERVAÇÃO:");
            System.out.println(trocoInfo);
            System.out.println("----------------------------------------");

        } else {
            // Cartão ou PayPal
            System.out.println("Pagamento: " + corrida.getFormaPagamento().getDescricao() + " (Via Aplicativo)");
            System.out.printf("Valor: R$ %.2f\n", corrida.getValor());
            System.out.println("\nO pagamento já foi processado/será debitado automaticamente. O motorista não precisa cobrar.");
        }

        System.out.println("\nPressione ENTER para iniciar a simulação da viagem.");
        ViewUtils.sc.nextLine();
    }
    private static void acompanharCorridaPassageiro(Corrida corrida) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("---- Acompanhando sua Solicitação ----");

            Corrida corridaAtualizada = cs.buscarCorridaPorId(corrida.getId());
            if (corridaAtualizada == null) break;

            if (corridaAtualizada.getStatus() == StatusCorrida.SOLICITADA) {
                System.out.println("Status: Aguardando um motorista aceitar...");
                System.out.println("\nPressione 'C' e ENTER a qualquer momento para CANCELAR a busca.\n\n");
            try {

                if (System.in.available() > 0) {
                    String input = ViewUtils.sc.nextLine().toUpperCase();
                    if (input.equals("C")) {
                        cs.cancelarCorrida(corrida, corrida.getPassageiroId());
                        System.out.println("\nBusca cancelada. Pressione ENTER para voltar ao menu.");
                        ViewUtils.sc.nextLine();
                        return;
                    }
                }
                Thread.sleep(5000);

            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
            continue;
        }
        if (corridaAtualizada.getStatus() == StatusCorrida.EM_CURSO) {
                Motorista motorista = cs.getMotoristaById(corridaAtualizada.getMotoristaId());
                exibirDetalhesPagamentoAoAceite(corridaAtualizada, motorista);

            System.out.println("Status: Motorista Encontrado e a caminho!");
                System.out.println("Motorista: " + motorista.getNome());

                SimuladorViagem.prepararSimulacao(corridaAtualizada);
                MapaView.abrirMapa();
                SimuladorViagem.simular(corridaAtualizada);
                break;
            }

            if (corridaAtualizada.getStatus() == StatusCorrida.FINALIZADA || corridaAtualizada.getStatus() == StatusCorrida.CANCELADA) {
                System.out.println("Status: " + corridaAtualizada.getStatus());
                break;
            }

            System.out.println("\n(Atualizando em 5 segundos...)");
            try { Thread.sleep(5000); } catch (InterruptedException e) {}
        }
    }
}