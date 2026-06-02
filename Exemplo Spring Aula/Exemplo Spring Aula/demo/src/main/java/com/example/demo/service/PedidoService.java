package com.example.demo.service;

import com.example.demo.Entities.*;
import com.example.demo.Enums.StatusPedido;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.mapper.PedidoMapper;
import com.example.demo.repository.ICardapioRepository;
import com.example.demo.repository.IReservaRepository;
import com.example.demo.repository.IPedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PedidoService {

    private final IPedidoRepository pedidoRepository;
    private final IReservaRepository reservaRepository;
    private final ICardapioRepository itemRepository;
    private final PedidoMapper pedidoMapper;

    public PedidoService(IPedidoRepository pedidoRepository,
                         IReservaRepository reservaRepository,
                         ICardapioRepository itemRepository,
                         PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.reservaRepository = reservaRepository;
        this.itemRepository = itemRepository;
        this.pedidoMapper = pedidoMapper;
    }

    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        Long pedidoId = pedidoDTO.getId();
        if (pedidoId != null && pedidoRepository.existsById(pedidoId)) {
            throw new RuntimeException("Já existe um pedido com esse id");
        }

        long reservaId = Objects.requireNonNull(pedidoDTO.getReservaId(), "Id da reserva é obrigatório");

        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        long itemId = Objects.requireNonNull(pedidoDTO.getItemId(), "Id do item é obrigatório");

        Cardapio item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (pedidoDTO.getQuantidade() == null || pedidoDTO.getQuantidade() <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        BigDecimal total = item.getPreco()
                .multiply(BigDecimal.valueOf(pedidoDTO.getQuantidade()));

        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
        pedido.setReserva(reserva);
        pedido.setItem(item);
        pedido.setValorTotal(total);
        pedido.setStatus(pedidoDTO.getStatus() == null ? StatusPedido.CONFIRMADO : pedidoDTO.getStatus());
        pedido.setDataPedido(LocalDate.now());
        pedido.setHoraPedido(LocalTime.now());

        return pedidoRepository.save(pedido);
    }

    public Pedido atualizarPedido(Long id, PedidoDTO pedidoDTO) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getStatus() == StatusPedido.FINALIZADO) {
            throw new RuntimeException("Pedido finalizado não pode ser alterado");
        }

        if (pedidoDTO.getItemId() != null) {
            long itemId = pedidoDTO.getItemId();
            Cardapio item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item não encontrado"));
            pedido.setItem(item);
        }

        if (pedidoDTO.getQuantidade() != null) {
            if (pedidoDTO.getQuantidade() <= 0) {
                throw new RuntimeException("Quantidade inválida");
            }
            pedido.setQuantidade(pedidoDTO.getQuantidade());
        }

        if (pedido.getItem() == null) {
            throw new RuntimeException("Pedido deve possuir pelo menos um item");
        }

        if (pedidoDTO.getQuantidade() != null || pedidoDTO.getItemId() != null) {
            pedido.setValorTotal(pedido.getItem().getPreco().multiply(BigDecimal.valueOf(pedido.getQuantidade())));
        }

        if (pedidoDTO.getStatus() != null) {
            if (pedidoDTO.getStatus() == StatusPedido.CANCELADO) {
                if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
                    throw new RuntimeException("Somente pedidos confirmados podem ser cancelados");
                }
                pedido.setStatus(StatusPedido.CANCELADO);
            } else if (pedidoDTO.getStatus() == StatusPedido.FINALIZADO) {
                if (pedido.getStatus() != StatusPedido.CONFIRMADO) {
                    throw new RuntimeException("Somente pedidos confirmados podem ser finalizados");
                }
                pedido.setStatus(StatusPedido.FINALIZADO);
            } else if (pedidoDTO.getStatus() == StatusPedido.CONFIRMADO) {
                pedido.setStatus(StatusPedido.CONFIRMADO);
            }
        }

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return Optional.ofNullable(pedidoRepository.findAll()).orElseGet(List::of);
    }

    public Pedido buscarPorId(Long id) {
        long pedidoId = Objects.requireNonNull(id, "id não pode ser nulo");
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public List<Pedido> listarPorReserva(Long reservaId) {
        long reservaIdNotNull = Objects.requireNonNull(reservaId, "id da reserva não pode ser nulo");
        if (!reservaRepository.existsById(reservaIdNotNull)) {
            throw new RuntimeException("Reserva não encontrada");
        }
        return Optional.ofNullable(pedidoRepository.findByReservaId(reservaIdNotNull)).orElseGet(List::of);
    }

    public void deletar(Long id) {
        Pedido pedido = buscarPorId(id);
        if (pedido.getStatus() == StatusPedido.FINALIZADO) {
            throw new RuntimeException("Pedido finalizado não pode ser deletado");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}