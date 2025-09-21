package org.example.model.repository;

import org.example.model.entity.Localizacao;
import java.util.List;

/**
 * Repositório para gerenciar a persistência de entidades Localizacao em um arquivo JSON.
 */
public class LocalizacaoRepository {

    private final JsonRepository<Localizacao> localizacaoDB;

    public LocalizacaoRepository() {
        this.localizacaoDB = new JsonRepository<>("src/main/resources/data/localizacoes.json", Localizacao.class);
    }

    /**
     * Carrega a lista de todas as localizações do arquivo JSON.
     * @return Uma lista de objetos Localizacao.
     */
    public List<Localizacao> carregar() {
        return localizacaoDB.carregar();
    }

    /**
     * Adiciona uma nova localização à lista existente e salva no arquivo.
     * @param novaLocalizacao O objeto Localizacao a ser adicionado.
     */
    public void adicionar(Localizacao novaLocalizacao) {
        List<Localizacao> locais = carregar();

        boolean jaExiste = locais.stream()
                .anyMatch(l -> l.getDescricao().equalsIgnoreCase(novaLocalizacao.getDescricao()));

        if (!jaExiste) {
            locais.add(novaLocalizacao);
            localizacaoDB.salvar(locais);
        } else {
            System.out.println("\n[AVISO] Um local com a mesma descrição já existe.");
        }
    }
}