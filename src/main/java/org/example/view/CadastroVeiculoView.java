package org.example.view;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Motorista;
import org.example.model.entity.Veiculo;
import org.example.model.service.MotoristaService;
import org.example.model.service.VeiculoService;

import java.time.Year;

/**
 * View para a segunda etapa do cadastro de Motorista (dados do veículo).
 */
public class CadastroVeiculoView {

    private static final VeiculoService veiculoService = new VeiculoService();
    private static final MotoristaService motoristaService = new MotoristaService();

    /**
     * Executa o fluxo de cadastro do veículo, vinculando-o ao motorista.
     * @param motorista O motorista recém-cadastrado.
     */
    public static void executar(Motorista motorista) {
        ViewUtils.limparConsole();
        System.out.println("--- Cadastro de Motorista (Etapa 2/2: Dados do Veículo) ---");
        System.out.println("Olá, " + motorista.getNome() + "! Agora, vamos cadastrar seu veículo.");

        String marca = solicitarTexto("Marca do veículo (ex: Chevrolet)", "A marca não pode estar em branco.");
        if (marca == null) return;
        String modelo = solicitarTexto("Modelo do veículo (ex: Onix)", "O modelo não pode estar em branco.");
        if (modelo == null) return;
        String placa = solicitarPlaca();
        if (placa == null) return;
        String renavam = solicitarRenavam();
        if (renavam == null) return;
        String cor = solicitarTexto("Cor", "A cor não pode estar em branco.");
        if (cor == null) return;
        Integer anoFabricacao = solicitarAnoFabricacao();
        if (anoFabricacao == null) return;
        Integer numAssentos = solicitarNumAssentos();
        if (numAssentos == null) return;
        Float capacidadeMala = solicitarCapacidadeMala();
        if (capacidadeMala == null) return;
        Boolean premium = solicitarPremium();
        if (premium == null) return;

        CategoriaVeiculo categoria = veiculoService.determinarCategoria(anoFabricacao, numAssentos, capacidadeMala, premium);
        System.out.println("\n[INFO] Categoria do veículo determinada automaticamente: " + categoria.getNome());

        Veiculo novoVeiculo = new Veiculo(marca, modelo, placa, renavam, anoFabricacao, cor, capacidadeMala, numAssentos, premium, categoria);
        Veiculo veiculoCadastrado = veiculoService.cadastrar(novoVeiculo);

        if (veiculoCadastrado != null) {
            motorista.setIdVeiculo(veiculoCadastrado.getId());
            motoristaService.atualizar(motorista);
            System.out.println("\nMotorista e Veículo cadastrados com sucesso!");
        } else {
            System.out.println("\nNão foi possível finalizar o cadastro do veículo.");
        }
        System.out.println("Pressione ENTER para voltar ao menu...");
        ViewUtils.sc.nextLine();
    }

    private static String solicitarTexto(String prompt, String erro) {
        while (true) {
            System.out.print(prompt + ": ");
            String texto = ViewUtils.sc.nextLine();
            if (texto.equalsIgnoreCase("voltar")) return null;
            if (!texto.trim().isEmpty()) return texto;
            System.out.println("\n[ERRO] " + erro);
        }
    }

    private static String solicitarPlaca() {
        String placa = "";
        boolean placaValida = false;

        while (!placaValida) {
            System.out.print("Placa (ex: ABC-1234 ou ABC1D23): ");
            placa = ViewUtils.sc.nextLine();

            if (placa.equalsIgnoreCase("voltar")) return null;

            if (placa.trim().length() < 7) {
                System.out.println("\n[ERRO] A placa parece ser inválida.");
            } else {
                placaValida = true;
            }
        }
        return placa;
    }

    private static String solicitarRenavam() {
        String renavam = "";
        boolean renavamValido = false;

        while (!renavamValido) {
            System.out.print("Renavam: ");
            renavam = ViewUtils.sc.nextLine();

            if (renavam.equalsIgnoreCase("voltar")) return null;

            if (!renavam.matches("\\d+") || renavam.length() < 9) {
                System.out.println("\n[ERRO] O Renavam deve conter apenas números (pelo menos 9 dígitos).");
            } else {
                renavamValido = true;
            }
        }
        return renavam;
    }

    private static Integer solicitarAnoFabricacao() {
        Integer ano = null;
        boolean anoValido = false;

        while (!anoValido) {
            System.out.print("Ano de fabricação: ");
            String input = ViewUtils.sc.nextLine();

            if (input.equalsIgnoreCase("voltar")) return null;

            try {
                int anoTemp = Integer.parseInt(input);
                if (anoTemp > 1980 && anoTemp <= Year.now().getValue()) {
                    ano = anoTemp;
                    anoValido = true;
                } else {
                    System.out.println("\n[ERRO] Ano inválido. O ano deve ser entre 1981 e " + Year.now().getValue() + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Formato de ano inválido. Use apenas números.");
            }
        }
        return ano;
    }

    private static Integer solicitarNumAssentos() {
        Integer assentos = null;
        boolean assentosValidos = false;

        while (!assentosValidos) {
            System.out.print("Número de assentos (ex: 5): ");
            String input = ViewUtils.sc.nextLine();

            if (input.equalsIgnoreCase("voltar")) return null;

            try {
                int assentosTemp = Integer.parseInt(input);
                if (assentosTemp > 1 && assentosTemp < 10) {
                    assentos = assentosTemp;
                    assentosValidos = true;
                } else {
                    System.out.println("\n[ERRO] Número de assentos inválido.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Use apenas números.");
            }
        }
        return assentos;
    }

    private static Float solicitarCapacidadeMala() {
        Float capacidade = null;
        boolean capacidadeValida = false;

        while (!capacidadeValida) {
            System.out.print("Capacidade do porta-malas em litros (ex: 280.5): ");
            String input = ViewUtils.sc.nextLine();

            if (input.equalsIgnoreCase("voltar")) return null;

            try {
                float capacidadeTemp = Float.parseFloat(input.replace(",", "."));
                if (capacidadeTemp > 0) {
                    capacidade = capacidadeTemp;
                    capacidadeValida = true;
                } else {
                    System.out.println("\n[ERRO] Capacidade inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Use apenas números.");
            }
        }
        return capacidade;
    }

    private static Boolean solicitarPremium() {
        Boolean premium = null;
        boolean opcaoValida = false;

        while (!opcaoValida) {
            System.out.print("É um veículo premium/de luxo? (S/N): ");
            String input = ViewUtils.sc.nextLine();

            if (input.equalsIgnoreCase("voltar")) return null;

            if (input.equalsIgnoreCase("S")) {
                premium = true;
                opcaoValida = true;
            } else if (input.equalsIgnoreCase("N")) {
                premium = false;
                opcaoValida = true;
            } else {
                System.out.println("\n[ERRO] Opção inválida. Digite 'S' para Sim ou 'N' para Não.");
            }
        }
        return premium;
    }
}