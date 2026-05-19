package com.example.demo.service;

import com.example.demo.Entities.*;
import com.example.demo.repository.ICardapioRepository;
import com.example.demo.repository.IReservaRepository;
import com.example.demo.repository.IPedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IReservaRepository reservaRepository;
    private final ICardapioRepository itemRepository;

    public PedidoService(IPedidoRepository pedidoRepository,
                         IReservaRepository reservaRepository,
                         ICardapioRepository itemRepository) {
        this.pedidoRepository = pedidoRepository;
        this.reservaRepository = reservaRepository;
        this.itemRepository = itemRepository;
    }

    public Pedido criarPedido(Long reservaId, Long itemId, Integer quantidade) {

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva nao encontrada"));

        Cardapio item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item nao encontrado"));

        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade invalida");
        }

        BigDecimal total = item.getPreco()
                .multiply(BigDecimal.valueOf(quantidade));

        Pedido pedido = new Pedido();
        pedido.setReserva(reserva);
        pedido.setItem(item);
        pedido.setQuantidade(quantidade);
        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado"));
    }

    public List<Pedido> listarPorReserva(Long reservaId) {
        return pedidoRepository.findByReservaId(reservaId);
    }

    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
}