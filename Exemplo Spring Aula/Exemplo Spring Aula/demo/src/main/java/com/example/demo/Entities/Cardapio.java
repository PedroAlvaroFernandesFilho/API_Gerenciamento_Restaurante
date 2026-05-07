package com.example.demo.Entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "itens_cardapio")
@NoArgsConstructor
@AllArgsConstructor

public class Cardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cardapio")
    private Long id;

    @Column(nullable = false)
    private String nome;

     @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;
}