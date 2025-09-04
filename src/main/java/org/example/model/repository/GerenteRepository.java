package org.example.model.repository;

import org.example.model.entity.Gerente;
import org.example.model.entity.Motorista;
import org.example.model.entity.Usuario;

import java.util.List;

public class GerenteRepository implements UsuarioRepository{

    private static JsonRepository<Gerente> gerentesDB = new JsonRepository<Gerente>("src/main/resources/data/gerentes.json", Gerente.class);
    private static List<Gerente> gerentesCarregados = gerentesDB.carregar();

    public void salvarGerente(Gerente gerente) {
        atualizarGerentesCarregados();
        int currentId = gerentesCarregados.size()+1;
        gerente.setId(currentId);
        gerentesCarregados.add(gerente);
        gerentesDB.salvar(gerentesCarregados);
    }


    @Override
    public Gerente buscarPorCpf(String cpfBusca) {
        atualizarGerentesCarregados();

        for (Gerente g : gerentesCarregados){
            if (g.getCpf().equals(cpfBusca)){
                return g;
            }
        }

        return null;
    }

    @Override
    public Gerente buscarPorEmail(String email) {
        atualizarGerentesCarregados();

        for (Gerente g : gerentesCarregados){
            if (g.getEmail().equals(email)){
                return g;
            }
        }

        return null;
    }

    @Override
    public void remover(String cpf) {
        atualizarGerentesCarregados();
        for (Gerente g : gerentesCarregados){
            if (g.getCpf().equals(cpf)){
                gerentesCarregados.remove(g);
                gerentesDB.salvar(gerentesCarregados);
            }
        }
    }

    @Override
    public boolean verificarEmail(String email) {
        atualizarGerentesCarregados();
        for (Gerente g : gerentesCarregados){
            if (g.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }


    public List<Gerente> getGerentes() {
        atualizarGerentesCarregados();
        return gerentesCarregados;
    }

    public Boolean existeCpf(String cpfBusca){
        atualizarGerentesCarregados();

        for (Gerente g : gerentesCarregados){
            if (g.getCpf().equals(cpfBusca)){
                return true;
            }
        }

        return false;
    }

    public static void atualizarGerentesCarregados(){
        gerentesCarregados = gerentesDB.carregar();
    }

    @Override
    public boolean realizarLogin(String email, String senha) {
        atualizarGerentesCarregados();
        for (Gerente g : gerentesCarregados){
            if (g.getEmail().equals(email) && g.getSenha().equals(senha)){
                return true;
            }
        }
        return false;
    }

    public static int getIdByCpf(String cpf) {
        atualizarGerentesCarregados();
        for (Gerente g : gerentesCarregados){
            if (g.getCpf().equals(cpf)){
                return g.getId();
            }
        }

        return 0;
    }
}
