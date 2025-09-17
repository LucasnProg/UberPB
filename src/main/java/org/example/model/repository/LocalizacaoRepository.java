package org.example.model.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.entity.Localizacao;
import org.example.model.entity.Motorista;
import org.example.util.LocalError;

import java.io.FileReader;
import java.util.List;

public class LocalizacaoRepository implements Repository {

    private static JsonRepository<Localizacao> localsDb =
            new JsonRepository<>("src/main/resources/data/localizacoes.json", Localizacao.class);

    private static List<Localizacao> localizacoesCarregadas = localsDb.carregar();

    @Override
    public void salvar(List localizacoes) {
        atualizarLocalizacoesCarregadas();

        localsDb.salvar(localizacoesCarregadas);
    }

    @Override
    public List<Localizacao> carregar() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("src/main/resources/data/localizacoes.json")) {
            return gson.fromJson(reader, new TypeToken<List<Localizacao>>(){}.getType());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void atualizarLocalizacoesCarregadas() {
        localizacoesCarregadas = localsDb.carregar();
    }


    public boolean verificarLocalizacao(Localizacao local){
        atualizarLocalizacoesCarregadas();
        if (localizacoesCarregadas.contains(local)){
            return true;
        } else {
            return false;
        }
    }

    public void adicionarLocalizacao(Localizacao local){
        atualizarLocalizacoesCarregadas();
        try{
            if(!verificarLocalizacao(local)){
                localizacoesCarregadas.add(local);
                localsDb.salvar(localizacoesCarregadas);
            } else {
                throw new LocalError("Esse local, ja está cadastrado, verifique e tente novamente");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
