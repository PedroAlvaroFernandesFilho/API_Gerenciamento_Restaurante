package com.example.demo.dto;

import com.example.demo.Enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class PedidoDTO {

    private Long id;

    @NotNull(message = "O id da reserva é obrigatório")
    private Long reservaId;

    @NotNull(message = "O id do item é obrigatório")
    private Long itemId;

    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Integer quantidade;
    private BigDecimal valorTotal;
    private StatusPedido status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPedido;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaPedido;
}
