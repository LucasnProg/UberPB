package org.example.model.repository;

import org.example.model.entity.Corrida;
import java.util.List;

/**
 * Repositório responsável pela persistência de dados da entidade Corrida.
 * Gerencia a leitura e escrita de corridas em um arquivo JSON.
 */
public class CorridaRepository implements Repository<Corrida> {

    private final JsonRepository<Corrida> corridasDB;
    private List<Corrida> corridasCarregadas;

    /**
     * Construtor padrão para uso em produção.
     * Aponta para o arquivo de dados principal.
     */
    public CorridaRepository() {
        this("src/main/resources/data/corridas.json");
    }

    /**
     * Construtor alternativo para uso em testes.
     * Permite especificar um arquivo JSON de teste para isolar os dados.
     * @param caminhoArquivoJson O caminho para o arquivo JSON de teste.
     */
    public CorridaRepository(String caminhoArquivoJson) {
        this.corridasDB = new JsonRepository<>(caminhoArquivoJson, Corrida.class);
        this.corridasCarregadas = corridasDB.carregar();
    }

    /**
     * Salva uma nova corrida no arquivo JSON.
     * Gera um novo ID para a corrida de forma robusta.
     * @param novaCorrida A corrida a ser adicionada.
     */
    public void salvar(Corrida novaCorrida) {
        atualizarCorridasCarregadas();
        // MUDANÇA: Lógica de geração de ID mais robusta para evitar reutilização após deleção.
        int proximoId = corridasCarregadas.stream()
                .mapToInt(Corrida::getId)
                .max()
                .orElse(0) + 1;

        novaCorrida.setId(proximoId);
        corridasCarregadas.add(novaCorrida);
        corridasDB.salvar(corridasCarregadas);
    }

    /**
     * Atualiza uma corrida existente no arquivo JSON.
     * @param corridaAtualizada O objeto Corrida com os dados atualizados.
     */
    public void atualizar(Corrida corridaAtualizada) {
        atualizarCorridasCarregadas();
        for (int i = 0; i < corridasCarregadas.size(); i++) {
            if (corridasCarregadas.get(i).getId() == corridaAtualizada.getId()) {
                corridasCarregadas.set(i, corridaAtualizada);
                corridasDB.salvar(corridasCarregadas);
                return;
            }
        }
    }

    /**
     * Remove uma corrida do arquivo JSON com base no ID.
     * @param id O ID da corrida a ser removida.
     */
    public void remover(int id) {
        atualizarCorridasCarregadas();
        corridasCarregadas.removeIf(c -> c.getId() == id);
        corridasDB.salvar(corridasCarregadas);
    }

    /**
     * Busca e retorna uma corrida pelo seu ID.
     * @param idBusca O ID da corrida a ser procurada.
     * @return O objeto Corrida encontrado ou null se não existir.
     */
    public Corrida buscarPorId(int idBusca) {
        atualizarCorridasCarregadas();
        return corridasCarregadas.stream()
                .filter(c -> c.getId() == idBusca)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retorna a lista completa de todas as corridas, garantindo que os dados estejam atualizados.
     * @return Uma lista de todas as corridas.
     */
    public List<Corrida> getCorridas() {
        atualizarCorridasCarregadas();
        return corridasCarregadas;
    }

    /**
     * Carrega a lista de corridas do arquivo JSON.
     * Implementação da interface Repository.
     * @return A lista de corridas carregada.
     */
    @Override
    public List<Corrida> carregar() {
        return corridasDB.carregar();
    }

    /**
     * Salva a lista completa de corridas no arquivo JSON.
     * Implementação da interface Repository.
     * @param entidades A lista de corridas a ser salva.
     */
    @Override
    public void salvar(List<Corrida> entidades) {
        corridasDB.salvar(entidades);
    }

    /**
     * Método privado para recarregar a lista de corridas do arquivo,
     * garantindo que a instância em memória esteja sempre sincronizada.
     */
    private void atualizarCorridasCarregadas() {
        this.corridasCarregadas = corridasDB.carregar();
    }
}