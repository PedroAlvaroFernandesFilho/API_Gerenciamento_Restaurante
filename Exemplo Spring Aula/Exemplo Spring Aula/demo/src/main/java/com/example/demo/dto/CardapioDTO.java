package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CardapioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "Descrição é obrigatório")
    private String descricao;

    @NotNull(message = "Preço é obrigatório")
    private BigDecimal preco;
}