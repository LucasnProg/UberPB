package org.example.view;

import org.example.model.entity.Entregador;
import org.example.model.entity.Motocicleta;
import org.example.model.service.EntregadorService;
import org.example.model.service.MotocicletaService;

import java.time.Year;

public class CadastroMotocicletaView {
    private static final MotocicletaService motocicletaService = new MotocicletaService();
    private static final EntregadorService entregadorService = new EntregadorService();

    /**
     * Executa o fluxo de cadastro do veículo, vinculando-o ao motorista.
     * @param entregador O motorista recém-cadastrado.
     */
    public static void executar(Entregador entregador) {
        ViewUtils.limparConsole();
        System.out.println("--- Cadastro de Entregador (Etapa 2/2: Dados do Veículo) ---");
        System.out.println("Olá, " + entregador.getNome() + "! Agora, vamos cadastrar seu veículo.\n");

        String fabricante = solicitarTexto("Marca do veículo (ex: Honda)", "A marca não pode estar em branco.");
        if (fabricante == null) return;
        String modelo = solicitarTexto("Modelo do veículo (ex: POP 110)", "O modelo não pode estar em branco.");
        if (modelo == null) return;
        String placa = solicitarPlaca();
        if (placa == null) return;

        Motocicleta novaMoto = new Motocicleta(fabricante, modelo, placa);
        Motocicleta motoCadastrada = motocicletaService.cadastrar(novaMoto);

        if (motoCadastrada != null) {
            entregador.setIdMotocicleta(motoCadastrada.getId());
            entregadorService.atualizar(entregador);
            System.out.println("\nMotorista e Motocicleta cadastradas com sucesso!");
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


}
