package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MesaDTO {

    private Long id;

    @NotNull(message = "Numero da mesa e obrigatorio")
    @Min(value = 1, message = "O numero da mesa deve ser maior que 0 (zero)")
    @Max(value = 100000, message = "O numero da mesa excedeu o tamanho permitido")
    private Integer numero_mesa;
    
    @NotNull(message = "Capacidade da mesa e obrigatoria")
    @Min(value = 1, message = "A capacidade deve ser maior que 0 (zero)")
    @Max(value = 50, message = "A capacidade excedeu o tamanho permitido")
    private Integer capacidade;

    @NotBlank(message = "Status da Mesa e obrigatorio")
    @Size(max = 20, message = "O Status inserido é muito longo.")
    private String status;

}
