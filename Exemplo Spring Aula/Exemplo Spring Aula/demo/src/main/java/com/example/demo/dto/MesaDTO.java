package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MesaDTO {

    private Long id;

    @NotNull(message = "Numero da mesa e obrigatorio")
    private Integer numero_mesa;
    
    @NotNull(message = "Capacidade da mesa e obrigatoria")
    private Integer capacidade;

    @NotBlank(message = "Status da Mesa e obrigatorio")
    private String status;

}
