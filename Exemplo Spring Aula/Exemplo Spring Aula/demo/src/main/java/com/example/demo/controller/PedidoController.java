package com.example.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PedidoDTO;
import com.example.demo.mapper.PedidoMaper;
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
    private final PedidoMaper PedidoMaper;

    public PedidoController(PedidoService pedidoService, PedidoMaper PedidoMaper) {
        this.pedidoService = pedidoService;
        this.PedidoMaper = PedidoMaper;
    }

    @Operation(summary = "Lista todos os pedidos", description = "Retorna todos os pedidos registrados")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> listarPedidos() {
        try {
            List<PedidoDTO> pedidoDTOs = pedidoService.listarTodos()
                    .stream()
                    .map(PedidoMaper::toDTO)
                    .collect(Collectors.toList());
            
            // Correção: Agora a lista está corretamente envelopada no ApiResponse
            return ResponseEntity.ok(new ApiResponse<>(pedidoDTOs)); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }

    @Operation(summary = "Lista pedidos por reserva", description = "Retorna todos os pedidos de uma reserva específica")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<ApiResponse<List<PedidoDTO>>> listarPorReserva(@PathVariable Long reservaId) {
        try {
            List<PedidoDTO> pedidos = pedidoService.listarPorReserva(reservaId)
                    .stream()
                    .map(PedidoMaper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(pedidos));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(new ErrorResponse("Erro ao buscar pedidos", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }

    @Operation(summary = "Cria um novo pedido", description = "Cadastra um pedido associado a uma reserva e a um item")
    @PostMapping
    public ResponseEntity<ApiResponse<PedidoDTO>> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        try {
            PedidoDTO savedPedido = PedidoMaper.toDTO(pedidoService.criarPedido(pedidoDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(savedPedido));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(new ErrorResponse("Argumento inválido", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }

    @Operation(summary = "Deleta um pedido", description = "Cancela logicamente um pedido pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletarPedido(@PathVariable Long id) {
        try {
            pedidoService.deletar(id);
            return ResponseEntity.ok(new ApiResponse<>("Pedido deletado com sucesso"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(new ErrorResponse("Erro ao deletar pedido", e.getMessage())));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(new ErrorResponse("Erro interno", e.getMessage())));
        }
    }
}