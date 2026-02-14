package org.example.view;

import org.example.model.entity.MenuItem;
import org.example.model.entity.Restaurante;
import org.example.model.service.RestauranteService;

import java.util.ArrayList;


public class GerenciarCarpadioView {
    private static final RestauranteService rs = new RestauranteService();


    public static void executar(Restaurante restaurante) {
        while (true) {
            ViewUtils.limparConsole();
            ArrayList<MenuItem> cardapio = null;
            System.out.println("--- Cardapio cadastrado ---");
            try{
                cardapio = rs.getMenu(restaurante.getId());
            } catch (NullPointerException e){
                System.out.println("\nRestaurante Ainda não possui cardápio cadastrado.");
            }
            if (cardapio!= null){
                for (MenuItem menuItem : cardapio) {
                    System.out.println("Id: "+menuItem.getId());
                    System.out.println("Nome: "+menuItem.getNome());
                    System.out.println("Ingredientes: "+menuItem.getIngredientes());
                    System.out.println("Preço: "+menuItem.getPreco());
                    System.out.println("Tempo de Preparo: "+menuItem.getTempoPreparo());
                }
            } else {
                System.out.println("\nCardápio vazio.");
            }

            int opcao = -1;
            while (opcao != 0) {
                ViewUtils.limparConsole();
                System.out.println("\n-----------------------");
                System.out.println("1 - Adicionar Item ao Cadarpio");
                System.out.println("2 - Remover Item do Cardapio");
                System.out.println("0 - Voltar");
                System.out.print("\nEscolha uma opção: ");

                try {
                    opcao = Integer.parseInt(ViewUtils.sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\n[ERRO] Entrada inválida. Pressione ENTER para tentar novamente.");
                    ViewUtils.sc.nextLine();
                }

                switch (opcao) {
                    case 1:
                        adicionarItensCardapio(restaurante);
                        ViewUtils.sc.nextLine();
                        break;
                    case 2:
                        try{
                            rs.getMenu(restaurante.getId());
                            removerItemDoCardapio(restaurante);
                            ViewUtils.sc.nextLine();
                            break;
                        } catch (NullPointerException e){
                            System.out.println("\nAinda não existem itens a serem removidos.");
                        }
                        return;
                    case 0:
                        return;
                    default:
                        System.out.println("\n[ERRO] Opção inválida!");
                        ViewUtils.sc.nextLine();
                }
            }

        }

    }

    public static void adicionarItensCardapio(Restaurante restaurante){
        ViewUtils.limparConsole();
        System.out.println("--- Cadastro de Itens no cardapio ---");
        System.out.println("(Digite 'voltar' a qualquer momento para cancelar)");
        String nome, ingredientes;
        double precoItem;
        int tempoPreparo;

        while (true) {
            System.out.print("Nome do Item: ");
            nome = ViewUtils.sc.nextLine();
            if (nome.equalsIgnoreCase("voltar")) return;
            if (!nome.trim().isEmpty()) break;
            System.out.println("\n[ERRO] O nome não pode estar em branco.");
        }

        while (true) {
            System.out.print("Ingredientes (Digite os ingredientes do item separados por virgulas): ");
            ingredientes = ViewUtils.sc.nextLine();
            if (ingredientes.equalsIgnoreCase("voltar")) return;
            if (!ingredientes.isEmpty()) break;
            System.out.println("\n[ERRO] Formato de CPF inválido.");
        }

        while (true) {
            System.out.print("Preço do item: ");
            String precoInput = ViewUtils.sc.nextLine();
            if (precoInput.equalsIgnoreCase("voltar")) return;
            try {
                precoItem = Double.parseDouble(precoInput.replace(",", "."));
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("\n[ERRO] Formato preço inválido.");
            }

        }

        while (true) {
            System.out.print("Tempo de preparo(insira o valor do tempo em minutos): ");
            String tempoInput = ViewUtils.sc.nextLine();
            if (tempoInput.equalsIgnoreCase("voltar")) return;
            try {
                tempoPreparo = Integer.parseInt(tempoInput);
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("\n[ERRO] Formato de tempo inválido.");
            }
        }

        MenuItem item = new MenuItem(nome, ingredientes, precoItem, tempoPreparo);
        rs.adicionarItemAoCardapio(restaurante, item);

        System.out.println("\nPressione ENTER para continuar...");
        ViewUtils.sc.nextLine();
    }

    public static void removerItemDoCardapio(Restaurante restaurante){
        ViewUtils.limparConsole();
        System.out.println("--- Remover um Item do cardapio ---");
        System.out.println("(Digite 'voltar' a qualquer momento para cancelar)");
        int idItem;
        while (true) {
            System.out.print("Id do Item a ser removido: ");
            String id = ViewUtils.sc.nextLine();
            if (id.equalsIgnoreCase("voltar")) return;
            try {
                idItem = Integer.parseInt(id);
                if (idItem>0 && idItem<=restaurante.getMenu().size()){
                    break;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("\n[ERRO] Formato de entrada inválido.");
            }
            System.out.println("\n[ERRO] Verifique o valor inserido e tente novamente.");
        }

        rs.removerItemPorId(restaurante, idItem);

        System.out.println("\nPressione ENTER para continuar...");
        ViewUtils.sc.nextLine();
    }
}
