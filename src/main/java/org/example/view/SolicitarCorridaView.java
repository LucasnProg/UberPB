// Arquivo: view/SolicitarCorridaView.java
package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.CorridaService;
import org.example.model.service.LocalizacaoService;
import org.example.model.service.PassageiroService;

import java.util.List;

/**
 * View dedicada ao fluxo de solicitação de uma nova corrida pelo passageiro.
 */
public class SolicitarCorridaView {

    private static final CorridaService cs = new CorridaService();
    private static final LocalizacaoService ls = new LocalizacaoService();
    private static final PassageiroService ps = new PassageiroService();

    /**
     * Executa o passo a passo para a solicitação de uma nova corrida.
     * @param passageiro O passageiro que está solicitando a corrida.
     */
    public static void executar(Passageiro passageiro) {
        ViewUtils.limparConsole();
        System.out.println("--- Solicitar Nova Corrida ---");

        List<Localizacao> locais = ls.carregarLocais();
        if (locais == null) {
            System.out.println("\n[ERRO] Não foi possível carregar os locais. Pressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        Localizacao origem = selecionarLocal("origem", locais);
        if (origem == null) return;

        locais = ls.carregarLocais();

        Localizacao destino = selecionarLocal("destino", locais);
        if (destino == null) return;

        if (origem.getDescricao().equalsIgnoreCase(destino.getDescricao())) {
            System.out.println("\n[ERRO] A origem e o destino não podem ser iguais. Pressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        CategoriaVeiculo categoria = CorridaView.selecionarCategoria();
        if (categoria == null) return;

        System.out.println("\nCalculando estimativa...");
        double estimativaValor = cs.calcularPrecoEstimado(origem, destino, categoria);
        System.out.printf("Preço Estimado: R$ %.2f\n", estimativaValor);

        System.out.print("\nDeseja confirmar a solicitação? (S/N): ");
        if (ViewUtils.sc.nextLine().equalsIgnoreCase("S")) {
            System.out.println("\nEnviando sua solicitação...");
            Corrida corridaSolicitada = cs.solicitarCorrida(passageiro, origem, destino, categoria);

            if (corridaSolicitada != null) {
                passageiro.getCorridasPendentes().add(corridaSolicitada);
                ps.atualizar(passageiro);

                System.out.println("\n[INFO] Solicitação enviada com sucesso!");
                System.out.println("Você pode acompanhar o status no menu 'Acompanhar Corridas Solicitadas'.");

            } else {
                System.out.println("\n[ERRO] Não foi possível processar sua solicitação no momento.");
            }
        } else {
            System.out.println("\nSolicitação cancelada.");
        }

        System.out.println("\nPressione ENTER para voltar ao menu principal.");
        ViewUtils.sc.nextLine();
    }


    /**
     * Exibe um menu para o usuário selecionar um local de uma lista OU cadastrar um novo.
     * @param tipo "origem" ou "destino", para customizar a mensagem.
     * @param locais A lista de Localizacao disponíveis.
     * @return A Localizacao escolhida, ou null se o usuário cancelar.
     */
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

    /**
     * Guia o usuário no processo de cadastrar uma nova localização com coordenadas.
     * @return A nova Localizacao cadastrada, ou null se o usuário cancelar.
     */
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

}