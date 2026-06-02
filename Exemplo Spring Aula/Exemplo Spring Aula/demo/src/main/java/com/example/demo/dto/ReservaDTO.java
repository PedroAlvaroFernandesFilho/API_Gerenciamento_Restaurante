package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class ReservaDTO {

    private Long id;

    @NotNull(message = "O Id do cliente e obrigatorio")
    @Min(value = 1, message = "O Id deve ser maior que 0 (zero) ")
    @Max(value = 999999999999L, message = "O Id do cliente excedeu o tamanho permitido")
    @Schema(example = "0")
    private Long cliente_Id;

    @NotNull(message = "O Id da mesa e obrigatorio")
    @Min(value = 1, message = "O Id deve ser maior que 0 (zero) ")
    @Max(value = 999999999999L, message = "O Id da Mesa excedeu o tamanho permitido")
    @Schema(example = "0")
    private Long mesa_Id;

    @Schema(type = "string", pattern = "dd/MM/yyyy", example = "02/06/2026", description = "Aceita formatos dd/MM/yyyy ou yyyy-MM-dd")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataReserva;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm")
    @Schema(type = "string", pattern = "HH:mm", example = "22:00")
    private LocalTime horaReserva;

    @NotBlank(message = "Status e obrigatorio")
    private String status;
}
