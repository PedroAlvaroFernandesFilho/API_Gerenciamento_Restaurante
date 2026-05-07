package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class PedidoDTO {

    private Long id;

    @NotBlank(message = "O Id da reserva e obrigatorio")
    private Long reservaId;

    @NotBlank(message = "O Id do item e obrigatorio")
    private Long itemId;

    @NotBlank(message = "A quantidade e obrigatoria")
    private Integer quantidade;

    private BigDecimal valorTotal;
}