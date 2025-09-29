package org.example.model.service;

import org.example.model.entity.Corrida;
import org.example.model.entity.Localizacao;
import org.example.model.entity.Motorista;
import org.example.model.repository.CorridaRepository;
import org.example.model.repository.MotoristaRepository;
import java.util.Random;

import java.util.List;

/**
 * Service responsável pela lógica de negócio relacionada a Motoristas.
 */
public class MotoristaService {

    private final MotoristaRepository motoristaRepository = new MotoristaRepository();
    private final LocalizacaoService localizacaoService = new LocalizacaoService();
    private final CorridaRepository corridaRepository = new CorridaRepository();

    /**
     * Tenta autenticar um motorista com base no email e senha.
     * @return O objeto Motorista se a autenticação for bem-sucedida, caso contrário null.
     */
    public Motorista login(String email, String senha) {
        Motorista motorista = motoristaRepository.buscarPorEmail(email);
        if (motorista != null && motorista.getSenha().equals(senha)) {
            simularLocalizacaoInicial(motorista);
            return motorista;
        }
        return null;
    }

    /**
     * Sorteia uma localização e a define como a posição atual do motorista.
     */
    private void simularLocalizacaoInicial(Motorista motorista) {
        List<Localizacao> locaisDisponiveis = localizacaoService.carregarLocais();
        if (locaisDisponiveis != null && !locaisDisponiveis.isEmpty()) {
            Random random = new Random();
            int indiceAleatorio = random.nextInt(locaisDisponiveis.size());
            Localizacao localizacaoSimulada = locaisDisponiveis.get(indiceAleatorio);
            motorista.setLocalizacao(localizacaoSimulada);
            motoristaRepository.atualizar(motorista);
        }
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
     * Motorista nega uma corrida notificada. Esta ação dispara a busca pelo próximo motorista.
     */
    public void negarCorrida(Motorista motorista, Corrida corrida) {
        Motorista motoristaAtualizado = motoristaRepository.buscarPorId(motorista.getId());
        if (motoristaAtualizado == null) return;
        motoristaAtualizado.getCorridasNotificadas().removeIf(c -> c.getId() == corrida.getId());
        motoristaRepository.atualizar(motoristaAtualizado);

        Corrida corridaAtual = corridaRepository.buscarPorId(corrida.getId());
        if (corridaAtual == null) return;
        corridaAtual.adicionarRejeicao(motorista.getId());
        corridaRepository.atualizar(corridaAtual);

        System.out.println("\nMotorista " + motorista.getNome() + " recusou. Procurando o próximo...");
        CorridaService corridaService = new CorridaService();
        corridaService.encontrarProximoMotoristaDisponivel(corridaAtual);
    }
}