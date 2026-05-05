package com.example.demo.repository;

import com.example.demo.Entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IPedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByReservaId(Long reservaId);

}