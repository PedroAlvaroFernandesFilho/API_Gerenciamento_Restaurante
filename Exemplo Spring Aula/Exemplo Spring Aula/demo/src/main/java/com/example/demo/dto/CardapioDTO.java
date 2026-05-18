package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CardapioDTO {

    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @NotBlank(message = "Descrição e obrigatorio")
    private String descricao;

    @NotNull(message = "Preço e obrigatorio")
    private BigDecimal preco;
}