// Arquivo: model/service/LocalizacaoService.java
package org.example.model.service;

import org.example.model.entity.Localizacao;
import org.example.model.repository.LocalizacaoRepository;
import java.util.List;

/**
 * Service que centraliza a lógica de negócio para a entidade Localizacao.
 */
public class LocalizacaoService {

    // Instância do repositório que gerencia os dados dos locais.
    private final LocalizacaoRepository localizacaoRepository = new LocalizacaoRepository();

    /**
     * Carrega e retorna a lista de todos os locais disponíveis a partir do repositório.
     * @return Uma lista de todos os locais cadastrados.
     */
    public List<Localizacao> carregarLocais() {
        return localizacaoRepository.carregar();
    }

    /**
     * Adiciona um novo local ao sistema.
     * @param local O objeto Localizacao a ser adicionado.
     */
    public void adicionarNovoLocal(Localizacao local) {
        if (local.getDescricao() == null || local.getDescricao().trim().isEmpty()) {
            System.out.println("\n[ERRO] A descrição do local não pode ser vazia.");
            return;
        }
        localizacaoRepository.adicionar(local);
    }
}