package org.example.model.repository;

import org.example.model.entity.Corrida;
import org.example.model.entity.Pedido;

import java.util.List;

/**
 * Repositório responsável pela persistência de dados da entidade Pedido.
 * Gerencia a leitura e escrita de pedidos em um arquivo JSON.
 */
public class PedidoRepository implements Repository<Pedido> {

    private final JsonRepository<Pedido> pedidosDB;
    private List<Pedido> pedidosCarregados;

    /**
     * Construtor padrão para uso em produção.
     * Aponta para o arquivo de dados principal.
     */
    public PedidoRepository() {
        this("src/main/resources/data/pedidos.json");
    }

    /**
     * Construtor alternativo para uso em testes.
     * Permite especificar um arquivo JSON de teste para isolar os dados.
     * @param caminhoArquivoJson O caminho para o arquivo JSON de teste.
     */
    public PedidoRepository(String caminhoArquivoJson) {
        this.pedidosDB = new JsonRepository<>(caminhoArquivoJson, Pedido.class);
        this.pedidosCarregados = pedidosDB.carregar();
    }

    /**
     * Salva um novo pedido no arquivo JSON.
     * Gera um novo ID para o pedido de forma robusta.
     * @param novoPedido o Pedido a ser adicionada.
     */
    public void salvar(Pedido novoPedido) {
        atualizarpedidosCarregados();
        int proximoId = pedidosCarregados.stream()
                .mapToInt(Pedido::getIdPedido)
                .max()
                .orElse(0) + 1;

        novoPedido.setIdPedido(proximoId);
        pedidosCarregados.add(novoPedido);
        pedidosDB.salvar(pedidosCarregados);
    }

    /**
     * Atualiza um pedido existente no arquivo JSON.
     * @param pedidoAtualizado O objeto pedido com os dados atualizados.
     */
    public void atualizar(Pedido pedidoAtualizado) {
        atualizarpedidosCarregados();
        for (int i = 0; i < pedidosCarregados.size(); i++) {
            if (pedidosCarregados.get(i).getIdPedido() == pedidoAtualizado.getIdPedido()) {
                pedidosCarregados.set(i, pedidoAtualizado);
                pedidosDB.salvar(pedidosCarregados);
                return;
            }
        }
    }

    /**
     * Remove um pedido do arquivo JSON com base no ID.
     * @param id O ID do pedido a ser removida.
     */
    public void remover(int id) {
        atualizarpedidosCarregados();
        pedidosCarregados.removeIf(c -> c.getIdPedido() == id);
        pedidosDB.salvar(pedidosCarregados);
    }

    /**
     * Busca e retorna um Pedido pelo seu ID.
     * @param idBusca O ID do Pedido a ser procurada.
     * @return O objeto Pedido encontrado ou null se não existir.
     */
    public Pedido buscarPorId(int idBusca) {
        atualizarpedidosCarregados();
        return pedidosCarregados.stream()
                .filter(c -> c.getIdPedido() == idBusca)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna a lista completa de todas os pedidos, garantindo que os dados estejam atualizados.
     * @return Uma lista de todos os Pedidos.
     */
    public List<Pedido> getPedidos() {
        atualizarpedidosCarregados();
        return pedidosCarregados;
    }

    /**
     * Carrega a lista de pedidos do arquivo JSON.
     * Implementação da interface Repository.
     * @return A lista de pedidos carregados.
     */
    @Override
    public List<Pedido> carregar() {
        return pedidosDB.carregar();
    }

    /**
     * Salva a lista completa de pedidos no arquivo JSON.
     * Implementação da interface Repository.
     * @param entidades A lista de pedidos a ser salva.
     */
    @Override
    public void salvar(List<Pedido> entidades) {
        pedidosDB.salvar(entidades);
    }

    /**
     * Método privado para recarregar a lista de pedidos do arquivo,
     * garantindo que a instância em memória esteja sempre sincronizada.
     */
    private void atualizarpedidosCarregados() {
        this.pedidosCarregados = pedidosDB.carregar();
    }

}