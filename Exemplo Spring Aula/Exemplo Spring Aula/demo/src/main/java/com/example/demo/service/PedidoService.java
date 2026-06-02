package com.example.demo.service;

import com.example.demo.Entities.Cardapio;
import com.example.demo.Entities.Pedido;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.mapper.PedidoMaper;
import com.example.demo.repository.ICardapioRepository;
import com.example.demo.repository.IPedidoRepository;
import com.example.demo.repository.IReservaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
public class PedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IReservaRepository reservaRepository;
    private final ICardapioRepository itemRepository;
    private final PedidoMaper PedidoMapper;

    public PedidoService(IPedidoRepository pedidoRepository,
                        IReservaRepository reservaRepository,
                        ICardapioRepository itemRepository,
                        PedidoMaper PedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.reservaRepository = reservaRepository;
        this.itemRepository = itemRepository;
        this.PedidoMapper = PedidoMapper;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> listarPorReserva(long reservaId) {
        if (!reservaRepository.existsById(reservaId)) {
            throw new RuntimeException("Reserva não encontrada");
        }
        return pedidoRepository.findByReservaId(reservaId);
    }

    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        Long reservaId = Objects.requireNonNull(pedidoDTO.getReservaId(), "ReservaId não pode ser nulo");
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Long itemId = Objects.requireNonNull(pedidoDTO.getItemId(), "ItemId não pode ser nulo");
        Cardapio item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (pedidoDTO.getQuantidade() == null || pedidoDTO.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        Pedido pedido = PedidoMapper.toEntity(pedidoDTO);
        pedido.setReserva(reserva);
        pedido.setItem(item);
        BigDecimal valorTotal = item.getPreco().multiply(BigDecimal.valueOf(pedidoDTO.getQuantidade()));
        pedido.setValorTotal(valorTotal);
        if (pedido.getDataPedido() == null) {
            pedido.setDataPedido(LocalDate.now());
        }
        if (pedido.getHoraPedido() == null) {
            pedido.setHoraPedido(LocalTime.now());
        }

        return pedidoRepository.save(pedido);
    }

    public void deletar(long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(com.example.demo.Enums.StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

}