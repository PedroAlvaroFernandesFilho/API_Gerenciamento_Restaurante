package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

import com.example.demo.Enums.StatusItem;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Data
public class CardapioDTO {

    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 255, message = "Nome deve ter no maximo 255 caracteres")
    private String nome;

    @NotBlank(message = "Descricao e obrigatoria")
    @Size(max = 255, message = "Descricao deve ter no maximo 255 caracteres")
    private String descricao;

    @NotNull(message = "Preco e obrigatorio")
    @Positive(message = "Preco deve ser positivo")
    private BigDecimal preco;

    @Min(value = 0, message = "Estoque nao pode ser negativo")
    private Integer estoque;

    private StatusItem status;
}