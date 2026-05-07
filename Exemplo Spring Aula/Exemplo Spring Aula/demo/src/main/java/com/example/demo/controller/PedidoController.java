package com.example.demo.controller;

import com.example.demo.Entities.Pedido;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.service.PedidoService;
import com.example.demo.service.Utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
@RestController
@RequestMapping("api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Lista todos os pedidos", description = "Retorna uma lista com todos os pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarTodos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        List<PedidoDTO> dtos = pedidos.stream()
                .map(pedido -> {
                    PedidoDTO dto = new PedidoDTO();
                    dto.setId(pedido.getId());
                    dto.setReservaId(pedido.getReserva() != null ? pedido.getReserva().getId() : null);
                    dto.setItemId(pedido.getItem() != null ? pedido.getItem().getId() : null);
                    dto.setQuantidade(pedido.getQuantidade());
                    dto.setValorTotal(pedido.getValorTotal());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Busca pedidos por reserva", description = "Retorna os pedidos de uma reserva específica")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> buscarPorReserva(@PathVariable @NonNull Long reservaId) {
        try {
            List<Pedido> pedidos = pedidoService.listarPorReserva(reservaId);
            List<PedidoDTO> dtos = pedidos.stream()
                    .map(pedido -> {
                        PedidoDTO dto = new PedidoDTO();
                        dto.setId(pedido.getId());
                        dto.setReservaId(pedido.getReserva() != null ? pedido.getReserva().getId() : null);
                        dto.setItemId(pedido.getItem() != null ? pedido.getItem().getId() : null);
                        dto.setQuantidade(pedido.getQuantidade());
                        dto.setValorTotal(pedido.getValorTotal());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>(true, "Pedidos encontrados", dtos));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @Operation(summary = "Cria um novo pedido", description = "Cadastra um novo pedido para uma reserva")
    @PostMapping
    public ResponseEntity<ApiResponse<PedidoDTO>> criarPedido(@RequestParam @NonNull Long reservaId,
                                                              @RequestParam @NonNull Long itemId,
                                                              @RequestParam @NonNull Integer quantidade) {
        try {
            Pedido pedido = pedidoService.criarPedido(reservaId, itemId, quantidade);
            PedidoDTO dto = new PedidoDTO();
            dto.setId(pedido.getId());
            dto.setReservaId(pedido.getReserva() != null ? pedido.getReserva().getId() : null);
            dto.setItemId(pedido.getItem() != null ? pedido.getItem().getId() : null);
            dto.setQuantidade(pedido.getQuantidade());
            dto.setValorTotal(pedido.getValorTotal());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Pedido criado com sucesso", dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }
}
 