package org.example.model.entity;

import org.example.model.service.CorridaService;

public class Passageiro extends Usuario{



    public Passageiro(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
    }


    public void solicitarCorrida(String origem, String destino, String categoriaDeVeiculo){
        CorridaService.procurarCorrida(this.getId(), origem, destino, categoriaDeVeiculo);
    }
}
