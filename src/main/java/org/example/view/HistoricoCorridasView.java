package org.example.view;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.service.CorridaService;
import org.example.model.service.PassageiroService; // Import necessário se for recarregar o passageiro

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * View responsável por exibir o histórico de corridas finalizadas de um passageiro,
 * com opção de filtrar por categoria.
 */
public class HistoricoCorridasView {

    private static final CorridaService cs = new CorridaService();
    private static final PassageiroService ps = new PassageiroService();

    /**
     * Exibe o histórico de corridas, permitindo filtrar por categoria.
     * @param passageiro O passageiro cujo histórico será exibido.
     */
    public static void executar(Passageiro passageiro) {
        Passageiro passageiroAtualizado = ps.buscarPorId(passageiro.getId());
        if (passageiroAtualizado == null) {
            System.out.println("[ERRO] Não foi possível carregar os dados do passageiro.");
            return;
        }

        List<Corrida> historicoCompleto = passageiroAtualizado.getHistoricoCorridas();
        CategoriaVeiculo filtroCategoria = null;

        while (true) {
            ViewUtils.limparConsole();
            System.out.println("--- Seu Histórico de Corridas ---");

            if (filtroCategoria == null) {
                System.out.print("\nDeseja filtrar por categoria de veículo? (S/N): ");
                String resposta = ViewUtils.sc.nextLine();
                if (resposta.equalsIgnoreCase("S")) {
                    filtroCategoria = CorridaView.selecionarCategoria();
                    if (filtroCategoria == null) {
                        continue;
                    }
                } else if (!resposta.equalsIgnoreCase("N")) {
                    System.out.println("\n[ERRO] Opção inválida. Exibindo todas as categorias.");
                    filtroCategoria = null;
                }
            }

            List<Corrida> historicoFiltrado;
            if (filtroCategoria != null) {
                System.out.println("\nExibindo corridas da categoria: " + filtroCategoria.getNome());
                CategoriaVeiculo finalFiltroCategoria = filtroCategoria;
                historicoFiltrado = historicoCompleto.stream()
                        .filter(corrida -> corrida.getCategoriaVeiculo() == finalFiltroCategoria)
                        .collect(Collectors.toList());
            } else {
                System.out.println("\nExibindo todas as corridas.");
                historicoFiltrado = historicoCompleto;
            }

            if (historicoFiltrado.isEmpty()) {
                if (filtroCategoria != null) {
                    System.out.println("\nVocê não possui corridas concluídas nesta categoria.");
                } else {
                    System.out.println("\nVocê ainda não completou nenhuma corrida.");
                }
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");
                for (Corrida corrida : historicoFiltrado) {
                    System.out.println("\n---------------------------------");
                    if (corrida.getHoraFim() != null) {
                        System.out.println("Data: " + corrida.getHoraFim().format(formatter));
                    } else {
                        System.out.println("Data: Indisponível");
                    }
                    System.out.println("Origem: " + corrida.getOrigem().getDescricao());
                    System.out.println("Destino: " + corrida.getDestino().getDescricao());
                    System.out.printf("Valor Pago: R$ %.2f\n", corrida.getValor());
                    System.out.println("Categoria: " + corrida.getCategoriaVeiculo().getNome());
                    if (corrida.getFormaPagamento() != null) {
                        System.out.println("Forma de pagamento: " + corrida.getFormaPagamento().getDescricao());
                    }

                    Motorista motorista = cs.getMotoristaById(corrida.getMotoristaId());
                    if (motorista != null) {
                        System.out.println("Motorista: " + motorista.getNome());
                    }
                    System.out.println("---------------------------------");
                }
            }

            System.out.println("\nOpções:");
            System.out.println("1 - Filtrar novamente por outra categoria");
            System.out.println("2 - Ver todas as categorias");
            System.out.println("0 - Voltar ao Menu do Passageiro");
            System.out.print("Escolha uma opção: ");
            String opcaoFinal = ViewUtils.sc.nextLine();

            if (opcaoFinal.equals("0")) {
                break;
            } else if (opcaoFinal.equals("2")) {
                filtroCategoria = null;
            } else {
                filtroCategoria = null;
            }
        }
    }
}