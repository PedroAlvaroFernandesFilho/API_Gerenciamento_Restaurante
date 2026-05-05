package com.example.demo.mapper;

import com.example.demo.Entities.Pedido;
import com.example.demo.dto.PedidoDTO;

public class PedidoMapper {

    public static PedidoDTO toDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setReservaId(pedido.getReserva().getId());
        dto.setItemId(pedido.getItem().getId());
        dto.setQuantidade(pedido.getQuantidade());
        dto.setValorTotal(pedido.getValorTotal());
        return dto;
    }
}