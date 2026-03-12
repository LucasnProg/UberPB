package org.example.model.service;

import org.example.model.entity.CategoriaVeiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoServiceTest {

    private VeiculoService veiculoService;

    @BeforeEach
    void setUp() {
        veiculoService = new VeiculoService();
    }

    @Test
    void testDeterminarCategoria_DeveRetornarUberBlack_QuandoPremium() {
        CategoriaVeiculo categoria = veiculoService.determinarCategoria(2020, 4, 300, true);
        assertEquals(CategoriaVeiculo.UBER_BLACK, categoria, "Veículos premium devem ser classificados como UBER_BLACK.");
    }

    @Test
    void testDeterminarCategoria_DeveRetornarUberXl_QuandoMaisDe5Assentos() {
        CategoriaVeiculo categoria = veiculoService.determinarCategoria(2015, 6, 300, false);
        assertEquals(CategoriaVeiculo.UBER_XL, categoria, "Veículos com mais de 5 assentos devem ser UBER_XL.");
    }

    @Test
    void testDeterminarCategoria_DeveRetornarUberComfort_QuandoNovo() {
        int anoRecente = Year.now().getValue() - 3; // 3 anos de uso
        CategoriaVeiculo categoria = veiculoService.determinarCategoria(anoRecente, 4, 300, false);
        assertEquals(CategoriaVeiculo.UBER_COMFORT, categoria, "Veículos com até 6 anos devem ser UBER_COMFORT.");
    }

    @Test
    void testDeterminarCategoria_DeveRetornarUberBag_QuandoMalaGrande() {
        CategoriaVeiculo categoria = veiculoService.determinarCategoria(2010, 4, 450, false);
        assertEquals(CategoriaVeiculo.UBER_BAG, categoria, "Veículos com mala > 400L devem ser UBER_BAG.");
    }

    @Test
    void testDeterminarCategoria_DeveRetornarUberX_PorPadrao() {
        CategoriaVeiculo categoria = veiculoService.determinarCategoria(2010, 4, 300, false);
        assertEquals(CategoriaVeiculo.UBER_X, categoria, "Veículos que não atendem outros critérios devem ser UBER_X.");
    }

}