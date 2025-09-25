package org.example.model.repository;

import org.example.model.entity.Passageiro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroRepositoryTest {

    private PassageiroRepository repo;

    @BeforeEach
    void setUp() {
        // Criar um repositório usando arquivo de teste separado
        String caminhoTeste = "src/test/resources/data/passageiros-test.json";
        File file = new File(caminhoTeste);
        if (file.exists()) file.delete(); // limpa arquivo antigo

        repo = new PassageiroRepository() {
            // Override para usar arquivo de teste
            @Override
            public void salvar(Passageiro passageiro) {
                try {
                    java.lang.reflect.Field f = PassageiroRepository.class.getDeclaredField("passageiroDB");
                    f.setAccessible(true);
                    f.set(this, new JsonRepository<>(caminhoTeste, Passageiro.class));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                super.salvar(passageiro);
            }
        };
    }

    @Test
    void testSalvarEBuscarPorCpf() {
        Passageiro p = new Passageiro("Maria Silva", "maria@email.com", "senha123", "12345678900", "11988888888");
        repo.salvar(p);

        Passageiro encontrado = repo.buscarPorCpf("12345678900");
        assertNotNull(encontrado);
        assertEquals("Maria Silva", encontrado.getNome());
        assertEquals(1, encontrado.getId());
    }

    @Test
    void testSalvarEBuscarPorEmail() {
        Passageiro p = new Passageiro("João", "joao@email.com", "senha123", "98765432100", "11999999999");
        repo.salvar(p);

        Passageiro encontrado = repo.buscarPorEmail("joao@email.com");
        assertNotNull(encontrado);
        assertEquals("João", encontrado.getNome());
    }

    @Test
    void testAtualizarPassageiro() {
        Passageiro p = new Passageiro("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        repo.salvar(p);

        p.setNome("Lucas Silva");
        repo.atualizar(p);

        Passageiro atualizado = repo.buscarPorCpf("11122233344");
        assertEquals("Lucas Silva", atualizado.getNome());
    }

    @Test
    void testListarPassageiros() {
        Passageiro p1 = new Passageiro("Joana", "joana@email.com", "senha123", "10101010101", "11910101010");
        Passageiro p2 = new Passageiro("Pedro", "pedro@email.com", "senha123", "20202020202", "11920202020");

        repo.salvar(p1);
        repo.salvar(p2);

        List<Passageiro> passageiros = List.of(
                repo.buscarPorId(1),
                repo.buscarPorId(2)
        );

        assertEquals(2, passageiros.size());
        assertEquals("Joana", passageiros.get(0).getNome());
        assertEquals("Pedro", passageiros.get(1).getNome());
    }
}
