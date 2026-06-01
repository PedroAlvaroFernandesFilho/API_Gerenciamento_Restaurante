package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

import com.example.demo.Enums.StatusItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CardapioDTO {

    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @NotBlank(message = "Descricao e obrigatoria")
    private String descricao;

    @NotNull(message = "Preco e obrigatorio")
    private BigDecimal preco;

    @Min(value = 0, message = "Estoque nao pode ser negativo")
    private Integer estoque;

    private StatusItem status;
}