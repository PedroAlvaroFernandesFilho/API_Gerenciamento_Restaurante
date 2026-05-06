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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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

    @ManyToOne
    @JoinColumn(name= "cliente_id", nullable = false)
    private Cliente cliente_Id;

    @OneToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa_Id;

    @Column(nullable = false)
    private LocalDateTime dataReserva;
    
    @Column(nullable = false)
    private LocalTime horaReserva;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusReserva status = StatusReserva.Confirmada;

}
