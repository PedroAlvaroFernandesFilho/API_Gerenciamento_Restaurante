package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.ReservaDTO;

import java.util.Optional;
import java.util.List;


@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long>{
    
    Optional<Reserva> findById(Long id_reserva);
    
    Optional<ReservaDTO> findByClienteId(Long clienteId);
}
