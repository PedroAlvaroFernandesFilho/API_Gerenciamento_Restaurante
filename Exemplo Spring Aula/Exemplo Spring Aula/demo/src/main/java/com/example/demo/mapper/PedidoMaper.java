package com.example.demo.mapper;

import com.example.demo.Entities.Cardapio;
import com.example.demo.Entities.Pedido;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.PedidoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PedidoMaper {

    @Mapping(source = "reserva.id", target = "reservaId")
    @Mapping(source = "item.id", target = "itemId")
    PedidoDTO toDTO(Pedido pedido);

    @Mapping(source = "reservaId", target = "reserva")
    @Mapping(source = "itemId", target = "item")
    Pedido toEntity(PedidoDTO pedidoDTO);

    List<PedidoDTO> toDTOList(List<Pedido> pedidos);

    default Reserva mapReserva(Long id) {
        if (id == null) {
            return null;
        }
        Reserva reserva = new Reserva();
        reserva.setId(id);
        return reserva;
    }

    default Cardapio mapItem(Long id) {
        if (id == null) {
            return null;
        }
        Cardapio item = new Cardapio();
        item.setId(id);
        return item;
    }
}