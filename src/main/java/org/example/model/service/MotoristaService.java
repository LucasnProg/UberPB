package org.example.model.service;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.repository.MotoristaRepository;

/**
 * Service responsável pela lógica de negócio relacionada a Motoristas.
 */
public class MotoristaService {

    private final MotoristaRepository motoristaRepository = new MotoristaRepository();

    /**
     * Tenta autenticar um motorista com base no email e senha.
     * @return O objeto Motorista se a autenticação for bem-sucedida, caso contrário null.
     */
    public Motorista login(String email, String senha) {
        Motorista motorista = motoristaRepository.buscarPorEmail(email);
        if (motorista != null && motorista.getSenha().equals(senha)) {
            return motorista;
        }
        return null;
    }

    /**
     * Cria um novo motorista no sistema após validar se o email ou CPF já existem.
     * @return O objeto Motorista recém-criado, ou null se o cadastro falhar.
     */
    public Motorista criar(String nome, String email, String senha, String cpf, String telefone) {
        if (motoristaRepository.buscarPorEmail(email) != null) {
            System.out.println("\n[ERRO] O e-mail informado já está cadastrado.");
            return null;
        }

        Motorista novoMotorista = new Motorista(nome, email, senha, cpf, telefone);
        motoristaRepository.salvar(novoMotorista);
        return motoristaRepository.buscarPorEmail(email);
    }

    /**
     * Atualiza os dados de um motorista no repositório.
     * @param motorista O objeto Motorista com os dados atualizados.
     */
    public void atualizar(Motorista motorista) {
        motoristaRepository.atualizar(motorista);
    }

    /**
     * Busca um motorista pelo seu ID.
     * @param id O ID do motorista.
     * @return O objeto Motorista encontrado, ou null.
     */
    public Motorista buscarPorId(int id) {
        return motoristaRepository.buscarPorId(id);
    }

    /**
     * Motorista nega uma corrida notificada. A corrida é removida de sua lista de notificações.
     * @param motorista O motorista que está negando a corrida.
     * @param corrida A corrida a ser negada.
     */
    public void negarCorrida(Motorista motorista, Corrida corrida) {
        Motorista motoristaAtualizado = motoristaRepository.buscarPorId(motorista.getId());
        if (motoristaAtualizado == null) return;

        motoristaAtualizado.getCorridasNotificadas().removeIf(c -> c.getId() == corrida.getId());
        motoristaRepository.atualizar(motoristaAtualizado);
    }
}