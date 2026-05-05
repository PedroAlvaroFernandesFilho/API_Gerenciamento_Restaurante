package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MesaDTO {

    private Long id;

    @NotBlank(message = "Numero da mesa e obrigatorio")
    private Integer numero_mesa;
    
    @NotBlank(message = "Capacidade da mesa e obrigatoria")
    private Integer capacidade;

    @NotBlank(message = "Status da Mesa e obrigatorio")
    private String status;

}
