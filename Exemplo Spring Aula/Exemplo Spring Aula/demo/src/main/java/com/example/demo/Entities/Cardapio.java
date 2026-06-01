package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

import com.example.demo.Enums.StatusItem;

@Data
@Entity
@Table(name = "itens_cardapio")
@NoArgsConstructor
@AllArgsConstructor
public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Integer estoque;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusItem status = StatusItem.DISPONIVEL;
}