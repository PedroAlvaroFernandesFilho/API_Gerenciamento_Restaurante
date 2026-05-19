package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    private LocalDate dataReserva;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaReserva;

    @NotBlank(message = "Status e obrigatorio")
    private String status;
}
