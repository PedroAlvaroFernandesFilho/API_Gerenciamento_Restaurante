package com.example.demo.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemDeCardapio item;

    @NotNull
    private Integer quantidade;

    private BigDecimal valorTotal;

    public void calcularValorTotal() {
        if (item == null || item.getPreco() == null || quantidade == null) {
            this.valorTotal = BigDecimal.ZERO;
            return;
        }
        this.valorTotal = item.getPreco().multiply(BigDecimal.valueOf(quantidade));
    }
}
// usar lombok para gerar os getters e setters automaticamente, evitando código boilerplate. , criar @table(name = "pedidos",) para definir o nome da tabela no banco de dados, adicionar validações como @NotNull para garantir que os campos obrigatórios sejam preenchidos, e implementar um método para calcular o valor total do pedido com base na quantidade e no preço do item.
 // usar @NoArgsConstructor @AllArgsConstructor para gerar construtores sem argumentos e com todos os argumentos, respectivamente.`