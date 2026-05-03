package com.example.demo.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "Mesa")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_mesa;

    @Column(nullable = false)
    private Integer numero_mesa;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(nullable = false)
    private String status = "Livre"; // perguntar ao professor se pode utilizar ENUM nesse ponto pois nesse sentido e o ideal

}
