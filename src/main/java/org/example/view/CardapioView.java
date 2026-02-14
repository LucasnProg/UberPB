package org.example.view;

import org.example.model.entity.MenuItem;
import org.example.model.entity.Passageiro;
import org.example.model.entity.Restaurante;
import org.example.model.service.RestauranteService;

import javax.swing.*;
import java.util.ArrayList;


public class CardapioView {
    private static final RestauranteService rs = new RestauranteService();
    private ArrayList<MenuItem> pedido = new ArrayList<>();


    public static ArrayList<MenuItem> executar(Restaurante restaurante) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("--- Monte seu pedido ---");
            try{
                for (MenuItem menuItem : rs.getMenu(restaurante.getId())) {
                    System.out.println("itens do cardapio");
                }

                System.out.print("\nEscolha uma opção (0 - Voltar/Cancelar): ");

                String input = ViewUtils.sc.nextLine();
            } catch (NullPointerException e){
                System.out.println("\nRestaurante Ainda não possui cardápio cadastrado. Pressione ENTER para tentar novamente.");
                ViewUtils.sc.nextLine();
                return null;
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Pressione ENTER para tentar novamente.");
                ViewUtils.sc.nextLine();
            }
        }
    }
}
