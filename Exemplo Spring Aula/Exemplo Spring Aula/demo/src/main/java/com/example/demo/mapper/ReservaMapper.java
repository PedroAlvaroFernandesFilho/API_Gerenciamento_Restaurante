package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Mesa;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.ReservaDTO;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(source = "cliente.id", target = "cliente_Id")
    @Mapping(source = "mesa_Id.id", target = "mesa_Id")
    ReservaDTO toDTO(Reserva reserva);

    @Mapping(source = "cliente_Id", target = "cliente")
    @Mapping(source = "mesa_Id", target = "mesa_Id")
    Reserva toEntity(ReservaDTO reservaDTO);

    List<ReservaDTO> toDTOList(List<Reserva> reserva);

    default Cliente mapCliente(Long id){
        if (id == null)
            return null;

        Cliente cliente = new Cliente();
        cliente.setId(id);

        return cliente;
    }

    default Mesa mapMesa(Long id) {
        if (id == null) 
            return null;

        Mesa mesa = new Mesa();
        mesa.setId(id);

        return mesa;
    }
}
