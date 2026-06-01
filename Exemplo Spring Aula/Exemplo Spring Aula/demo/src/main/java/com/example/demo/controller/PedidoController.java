package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PedidoDTO;
import com.example.demo.mapper.PedidoMapper;
import com.example.demo.service.PedidoService;
import com.example.demo.service.Utils.ApiResponse;
import com.example.demo.service.Utils.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Pedidos", description = "Endpoints para gerenciamento de pedidos")
@RestController
@RequestMapping("api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Lista todos os pedidos", description = "Retorna todos os pedidos registrados")
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        List<PedidoDTO> pedidoDTOs = pedidoService.listarTodos()
                .stream()
                .map(PedidoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidoDTOs);
    }

    @Operation(summary = "Busca um pedido por ID", description = "Retorna os dados de um pedido específico")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable Long id) {
        PedidoDTO dto = PedidoMapper.toDTO(pedidoService.buscarPorId(id));
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Cria um novo pedido", description = "Registra um pedido vinculado a reserva e item do cardápio")
    @PostMapping
    public ResponseEntity<ApiResponse<PedidoDTO>> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        try {
            var pedido = pedidoService.criarPedido(pedidoDTO.getReservaId(), pedidoDTO.getItemId(), pedidoDTO.getQuantidade(), pedidoDTO.getId());
            PedidoDTO dto = PedidoMapper.toDTO(pedido);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(new ErrorResponse("Argumento inválido", e.getMessage())));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(new ErrorResponse("Erro ao criar pedido", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }

    @Operation(summary = "Atualiza um pedido", description = "Atualiza item, quantidade ou status do pedido")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidoDTO>> atualizarPedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        try {
            var pedido = pedidoService.atualizarPedido(id, pedidoDTO.getItemId(), pedidoDTO.getQuantidade(), pedidoDTO.getStatus());
            PedidoDTO dto = PedidoMapper.toDTO(pedido);
            return ResponseEntity.ok(new ApiResponse<>(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(new ErrorResponse("Erro ao atualizar pedido", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }

    @Operation(summary = "Lista pedidos por reserva", description = "Retorna todos os pedidos vinculados a uma reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> listarPorReserva(@PathVariable Long reservaId) {
        try {
            List<PedidoDTO> pedidos = pedidoService.listarPorReserva(reservaId)
                    .stream()
                    .map(PedidoMapper::toDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>(pedidos));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(new ErrorResponse("Erro ao listar pedidos", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }

    @Operation(summary = "Deleta um pedido", description = "Remove um pedido pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletarPedido(@PathVariable Long id) {
        try {
            pedidoService.deletar(id);
            return ResponseEntity.ok(new ApiResponse<>("Pedido deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(new ErrorResponse("Erro ao deletar pedido", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }
}
