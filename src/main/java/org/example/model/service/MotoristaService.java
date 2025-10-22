package org.example.model.service;

import org.example.model.entity.Corrida;
import org.example.model.entity.Localizacao;
import org.example.model.entity.Motorista;
import org.example.model.repository.CorridaRepository;
import org.example.model.repository.MotoristaRepository;
import java.util.List;
import java.util.Random;

/**
 * Service responsável pela lógica de negócio relacionada a Motoristas.
 */
public class MotoristaService {

    private final MotoristaRepository motoristaRepository = new MotoristaRepository();
    private final LocalizacaoService localizacaoService = new LocalizacaoService();
    private final CorridaRepository corridaRepository = new CorridaRepository();

    public Motorista login(String email, String senha) {
        Motorista motorista = motoristaRepository.buscarPorEmail(email);
        if (motorista != null && motorista.getSenha().equals(senha)) {
            simularLocalizacaoInicial(motorista);
            return motorista;
        }
        return null;
    }

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

    public Motorista criar(String nome, String email, String senha, String cpf, String telefone) {
        if (motoristaRepository.buscarPorEmail(email) != null) {
            System.out.println("\n[ERRO] O e-mail informado já está cadastrado.");
            return null;
        }

        Motorista novoMotorista = new Motorista(nome, email, senha, cpf, telefone);
        motoristaRepository.salvar(novoMotorista);
        return motoristaRepository.buscarPorEmail(email);
    }

    public void atualizar(Motorista motorista) {
        motoristaRepository.atualizar(motorista);
    }

    public Motorista buscarPorId(int id) {
        return motoristaRepository.buscarPorId(id);
    }

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

    public void recalcularMediaAvaliacao(Motorista motorista, int novaNota) {
        motorista.adicionarAvaliacao(novaNota);
        int totalAvaliacoes = motorista.getAvaliacoesRecebidas().size();
        if (totalAvaliacoes > 0) {
            double soma = motorista.getAvaliacoesRecebidas().stream()
                    .mapToInt(Integer::intValue)
                    .sum();
            double novaMedia = soma / totalAvaliacoes;
            motorista.setMediaAvaliacao(Math.round(novaMedia * 100.0) / 100.0);
        } else {
            motorista.setMediaAvaliacao(0.0);
        }
        motoristaRepository.atualizar(motorista);
    }

    /** ✅ Método adicionado para testes: limpa todos os motoristas */
    public void limpar() {
        motoristaRepository.limpar();
    }
}
