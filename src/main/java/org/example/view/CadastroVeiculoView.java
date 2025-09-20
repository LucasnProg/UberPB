package org.example.view;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Motorista;
import org.example.model.entity.Veiculo;
import org.example.model.service.MotoristaService;
import org.example.model.service.VeiculoService;

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
        // ... (lógica de coleta de dados do veículo com validações) ...
        String placa = "...", marca = "...", modelo = "...", cor = "...", renavam = "N/A";
        int ano = 0;

        CategoriaVeiculo categoria = CorridaView.selecionarCategoria();
        if (categoria == null) return;

        Veiculo novoVeiculo = new Veiculo(marca, modelo, placa, renavam, ano, cor, categoria);
        Veiculo veiculoCadastrado = veiculoService.cadastrar(novoVeiculo);

        if (veiculoCadastrado != null) {
            motorista.setIdVeiculo(veiculoCadastrado.getId());
            motoristaService.atualizar(motorista);
            System.out.println("\nMotorista e Veículo cadastrados com sucesso!");
        } else {
            // Futura melhoria: deletar o motorista órfão que foi criado.
            System.out.println("\nNão foi possível finalizar o cadastro do veículo.");
        }
        System.out.println("Pressione ENTER para voltar ao menu...");
        ViewUtils.sc.nextLine();
    }
}