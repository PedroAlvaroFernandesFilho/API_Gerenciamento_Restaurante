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

@Service
public class PedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IReservaRepository reservaRepository;
    private final ICardapioRepository itemRepository;
    private final PedidoMaper pedidoMapper;

    public PedidoService(IPedidoRepository pedidoRepository,
                        IReservaRepository reservaRepository,
                        ICardapioRepository itemRepository,
                        PedidoMaper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.reservaRepository = reservaRepository;
        this.itemRepository = itemRepository;
        this.pedidoMapper = pedidoMapper;
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
        return pedidoRepository.findByReservaId(reservaId);
    }

    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        Reserva reserva = reservaRepository.findById(pedidoDTO.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Cardapio item = itemRepository.findById(pedidoDTO.getItemId())
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (pedidoDTO.getQuantidade() == null || pedidoDTO.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
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

    public void deletar(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(com.example.demo.Enums.StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

}