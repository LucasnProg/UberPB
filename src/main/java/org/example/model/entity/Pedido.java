package org.example.model.entity;

import org.example.model.service.RestauranteService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int idPedido;
    private int idCliente;
    private int idRestaurante;
    private int idEntregador;
    private ArrayList<MenuItem> itensPedidos;
    private double valor;
    private FormaPagamento formaPagamento;
    private StatusCorrida statusPedido;
    private List<Integer> EntregadoresQueRejeitaram = new ArrayList<>();
    private Localizacao origem;
    private Localizacao destino;
    private LocalDateTime horaInicio;
    private LocalDateTime horaAceite;
    private LocalDateTime horaFim;
    private boolean precisaTroco;

    public Pedido() {}

    public Pedido(int idCliente, int idRestaurante, ArrayList<MenuItem> itensPedidos, LocalDateTime horaInicio) {
        this.idCliente = idCliente;
        this.idRestaurante = idRestaurante;
        this.itensPedidos = itensPedidos;
        this.origem = RestauranteService.getLocalizacaoPorID(idRestaurante);
        this.EntregadoresQueRejeitaram.add(0);
        this.horaInicio = horaInicio;
    }

    public Pedido(int idCliente, int idRestaurante, ArrayList<MenuItem> itensPedidos) {
        this.idCliente = idCliente;
        this.idRestaurante = idRestaurante;
        this.itensPedidos = itensPedidos;
        this.origem = RestauranteService.getLocalizacaoPorID(idRestaurante);
        this.EntregadoresQueRejeitaram.add(0);
        this.horaInicio = null;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEntregador() {
        return idEntregador;
    }

    public void setIdEntregador(int idEntregador) {
        this.idEntregador = idEntregador;
    }

    public ArrayList<MenuItem> getItensPedidos() {
        return itensPedidos;
    }

    public void setItensPedidos(ArrayList<MenuItem> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public StatusCorrida getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusCorrida statusPedido) {
        this.statusPedido = statusPedido;
    }

    public List<Integer> getEntregadoresQueRejeitaram() {
        return EntregadoresQueRejeitaram;
    }

    public void setEntregadoresQueRejeitaram(List<Integer> entregadoresQueRejeitaram) {
        EntregadoresQueRejeitaram = entregadoresQueRejeitaram;
    }

    public Localizacao getOrigem() {
        return origem;
    }

    public void setOrigem(Localizacao origem) {
        this.origem = origem;
    }

    public Localizacao getDestino() {
        return destino;
    }

    public void setDestino(Localizacao destino) {
        this.destino = destino;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraAceite() {
        return horaAceite;
    }

    public void setHoraAceite(LocalDateTime horaAceite) {
        this.horaAceite = horaAceite;
    }

    public LocalDateTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalDateTime horaFim) {
        this.horaFim = horaFim;
    }

    public boolean isPrecisaTroco() {
        return precisaTroco;
    }

    public void setPrecisaTroco(boolean precisaTroco) {
        this.precisaTroco = precisaTroco;
    }
}