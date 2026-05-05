package com.example.demo.service;

import com.example.demo.Entities.*;
import com.example.demo.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IReservaRepository reservaRepository;
    private final IItemDeCardapioRepository itemRepository;

    public PedidoService(IPedidoRepository pedidoRepository,
                         IReservaRepository reservaRepository,
                         IItemDeCardapioRepository itemRepository) {
        this.pedidoRepository = pedidoRepository;
        this.reservaRepository = reservaRepository;
        this.itemRepository = itemRepository;
    }

    public Pedido criarPedido(Long reservaId, Long itemId, Integer quantidade) {

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        ItemDeCardapio item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (quantidade == null || quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
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
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> listarPorReserva(Long reservaId) {
        return pedidoRepository.findByReservaId(reservaId);
    }

    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }
}