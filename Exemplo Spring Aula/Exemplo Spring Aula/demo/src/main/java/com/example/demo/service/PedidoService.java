package com.example.demo.service;

import com.example.demo.Entities.*;
import com.example.demo.Enums.StatusPedido;
import com.example.demo.repository.ICardapioRepository;
import com.example.demo.repository.IReservaRepository;
import com.example.demo.repository.IPedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public Pedido criarPedido(Long reservaId, Long itemId, Integer quantidade, Long id) {

        if (id != null && pedidoRepository.existsById(id)) {
            throw new RuntimeException("Já existe um pedido com esse id");
        }

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        if (itemId == null) {
            throw new RuntimeException("Pedido deve possuir pelo menos um item");
        }

        Cardapio item = itemRepository.findById(itemId)
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
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataPedido(LocalDate.now());
        pedido.setHoraPedido(LocalTime.now());

        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarPedido(Long id, Long itemId, Integer quantidade, StatusPedido novoStatus) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatus() == StatusPedido.FINALIZADO) {
            throw new RuntimeException("Pedido finalizado não pode ser alterado");
        }

        if (itemId != null) {
            Cardapio item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));
            pedido.setItem(item);
        }

        if (quantidade != null) {
            if (quantidade <= 0) {
                throw new RuntimeException("Quantidade inválida");
            }
            pedido.setQuantidade(quantidade);
        }

        if (pedido.getItem() == null) {
            throw new RuntimeException("Pedido deve possuir pelo menos um item");
        }

        if (quantidade != null || itemId != null) {
            pedido.setValorTotal(pedido.getItem().getPreco().multiply(BigDecimal.valueOf(pedido.getQuantidade())));
        }

        if (novoStatus != null) {
            if (novoStatus == StatusPedido.CANCELADO) {
                if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
                    throw new RuntimeException("Somente pedidos confirmados podem ser cancelados");
                }
                pedido.setStatus(StatusPedido.CANCELADO);
            } else if (novoStatus == StatusPedido.FINALIZADO) {
                if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
                    throw new RuntimeException("Somente pedidos confirmados podem ser finalizados");
                }
                pedido.setStatus(StatusPedido.FINALIZADO);
            } else if (novoStatus == StatusPedido.CONFIRMADO) {
                pedido.setStatus(StatusPedido.CONFIRMADO);
            }
        }

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
        if (!reservaRepository.existsById(reservaId)) {
            throw new RuntimeException("Reserva não encontrada");
        }
        List<Pedido> pedidos = pedidoRepository.findByReservaId(reservaId);
        if (pedidos == null || pedidos.isEmpty()) {
            throw new RuntimeException("Reserva não possui pedidos");
        }
        return pedidos;
    }

    public void deletar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getStatus() == StatusPedido.FINALIZADO) {
            throw new RuntimeException("Pedido finalizado não pode ser deletado");
        }
        pedidoRepository.deleteById(id);
    }
}