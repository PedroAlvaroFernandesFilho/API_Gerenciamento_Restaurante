package com.example.demo.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ReservaDTO {

      private Long id;

    @NotNull(message = "O Id do cliente e obrigatorio")
    private Long cliente_Id;

    @NotNull(message = "O Id da mesa e obrigatorio")
    private Long mesa_Id;

    private LocalDateTime dataReserva;

    private LocalTime horaReserva;

    @NotBlank(message = "Status e obrigatorio")
    private String status;
}
