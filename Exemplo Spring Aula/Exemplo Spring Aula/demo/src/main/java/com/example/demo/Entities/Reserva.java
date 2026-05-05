package com.example.demo.Entities;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.example.demo.Enums.StatusReserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "Reserva")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_reserva;

    @Column(nullable = false)
    private Long cliente_Id;

    @Column(nullable = false)
    private Long mesa_Id;

    @Column(nullable = false)
    private LocalDateTime dataReserva;
    
    @Column(nullable = false)
    private LocalTime horaReserva;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status = StatusReserva.Confirmada;

}
