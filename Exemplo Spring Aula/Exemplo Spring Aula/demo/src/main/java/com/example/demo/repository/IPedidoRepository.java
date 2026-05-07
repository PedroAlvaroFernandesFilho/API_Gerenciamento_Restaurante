package com.example.demo.repository;

import com.example.demo.Entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IPedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<List<Pedido>> findByReservaId(Long reservaId);

    List<Pedido> findByItemId(Long itemId);

    List<Pedido> findByValorTotal(BigDecimal valorTotal);

    @Query("SELECT p FROM Pedido p WHERE p.reserva.id = :reservaId AND p.item.id = :itemId")
    Optional<List<Pedido>> findByReservaIdAndItemId(@Param("reservaId") Long reservaId, @Param("itemId") Long itemId);

    default Optional<List<Pedido>> findByReservaIdWithTryCatch(Long reservaId) {
        try {
            return findByReservaId(reservaId);
        } catch (Exception e) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Erro ao buscar pedidos por reserva: " + e.getMessage());
        }
    }

    default List<Pedido> findByItemIdWithTryCatch(Long itemId) {
        try {
            return findByItemId(itemId);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar pedidos por item: " + e.getMessage());
        }
    }

    default List<Pedido> findByValorTotalWithTryCatch(BigDecimal valorTotal) {
        try {
            return findByValorTotal(valorTotal);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar pedidos por valor total: " + e.getMessage());
        }
    }
}
// Usar opcional em vez de listar todos os pedidos, para evitar retornar uma lista vazia quando não houver pedidos para uma reserva específica. Implementar métodos personalizados para buscar pedidos por item ou por valor total, se necessário. ou usar trycatch para tratar exceções ao buscar pedidos por reserva, e retornar uma resposta adequada para o cliente, como um status 404 (Not Found) se não houver pedidos para a reserva especificada.