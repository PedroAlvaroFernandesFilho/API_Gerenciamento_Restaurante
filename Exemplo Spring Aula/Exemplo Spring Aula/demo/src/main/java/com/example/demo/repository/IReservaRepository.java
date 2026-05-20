package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Reserva;
import com.example.demo.Enums.StatusReserva;

import java.util.Optional;
import java.util.List;


@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long>{
    
    Optional<Reserva> findById(Long id_reserva);
    
    List<Reserva> findByClienteId(Long id);

    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.mesa_Id.id = :mesaId AND r.status NOT IN (:status1, :status2)")
    long countReservasAtivas(
        @Param("mesaId") Long mesaId,
        @Param("status1") StatusReserva status1,
        @Param("status2") StatusReserva status2
    );
}
