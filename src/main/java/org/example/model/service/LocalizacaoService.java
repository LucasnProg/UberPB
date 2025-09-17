package org.example.model.service;

import org.example.model.entity.Localizacao;
import org.example.model.repository.LocalizacaoRepository;

import java.util.List;

/**
 * A classe LocalizacaoService serve como uma camada intermediária para acessar
 * os dados de localização, seguindo o padrão de arquitetura do projeto.
 * Ela delega as chamadas para a classe LocalizacaoRepository.
 */
public class LocalizacaoService {

    // Instância do repositório que gerencia os dados dos locais.
    private static final LocalizacaoRepository localizacaoRepository = new LocalizacaoRepository();

    /**
     * Carrega e retorna a lista de todos os locais disponíveis a partir do repositório.
     * Este é o método que será chamado pela CorridaView.
     *
     * @return Uma lista (List<Localizacao>) de todos os locais cadastrados.
     * Retorna null se ocorrer um erro ao carregar os dados.
     */
    public static List<Localizacao> carregarLocais() {
        // A lógica de negócio é simplesmente delegar a chamada para a camada de dados.
        return localizacaoRepository.carregar();
    }

    /**
     * Adiciona um novo local ao sistema, se ele ainda não existir.
     *
     * @param local O objeto Localizacao a ser adicionado.
     */
    public void adicionarNovoLocal(Localizacao local) {
        // Delega a responsabilidade de adicionar para o repositório.
        localizacaoRepository.adicionarLocalizacao(local);
    }

    /**
     * Verifica se um determinado local já existe no sistema.
     *
     * @param local O objeto Localizacao a ser verificado.
     * @return true se o local já existe, false caso contrário.
     */
    public boolean verificarSeLocalExiste(Localizacao local) {
        // Delega a verificação para o repositório.
        return localizacaoRepository.verificarLocalizacao(local);
    }
}