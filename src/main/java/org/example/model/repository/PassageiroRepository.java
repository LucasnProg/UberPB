package org.example.model.repository;

import org.example.model.entity.Passageiro;

import java.util.List;

public class PassageiroRepository implements UsuarioRepository{

    private JsonRepository<Passageiro> passageirosDB = new JsonRepository<Passageiro>("src/main/resources/data/passageiros.json", Passageiro.class);
    private List<Passageiro> passageirosCarregados = passageirosDB.carregar();



    @Override
    public Passageiro buscarPorCpf(String cpfBusca) {
        atualizarPassageirosCarregados();

        for (Passageiro p : passageirosCarregados){
            if (p.getCpf().equals(cpfBusca)){
                return p;
            }
        }

        return null;
    }

    public Passageiro buscarPorId(int id) {
        atualizarPassageirosCarregados();

        for (Passageiro p : passageirosCarregados){
            if (p.getId() == id){
                return p;
            }
        }

        return null;
    }

    @Override
    public void remover(String cpf) {
        atualizarPassageirosCarregados();
        for (Passageiro p : passageirosCarregados){
            if (p.getCpf().equals(cpf)){
                passageirosCarregados.remove(p);
                passageirosDB.salvar(passageirosCarregados);
            }
        }
    }

    public void salvarPassageiro(Passageiro passageiro) {
        atualizarPassageirosCarregados();
        int currentId = passageirosCarregados.size()+1;
        passageiro.setId(currentId);
        passageirosCarregados.add(passageiro);
        passageirosDB.salvar(passageirosCarregados);
    }


    public List<Passageiro> getPassageiros() {
        atualizarPassageirosCarregados();
        return passageirosCarregados;
    }

    public Boolean existeCpf(String cpfBusca){
        atualizarPassageirosCarregados();

        for (Passageiro p : passageirosCarregados){
            if (p.getCpf().equals(cpfBusca)){
                return true;
            }
        }

        return false;
    }

    public void atualizarPassageirosCarregados(){
        this.passageirosCarregados = passageirosDB.carregar();
    }
}
