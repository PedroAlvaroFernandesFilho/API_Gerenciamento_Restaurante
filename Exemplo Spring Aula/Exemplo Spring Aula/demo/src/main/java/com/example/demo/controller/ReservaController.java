package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ReservaDTO;
import com.example.demo.service.ReservaService;
import com.example.demo.service.Utils.ApiResponse;
import com.example.demo.service.Utils.ErrorResponse;
import com.example.demo.service.Utils.ReservaStatusRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "Reserva", description = "Endpoints para gerenciamento das Reservas")
@RestController
@RequestMapping("api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Operation(summary = "Cria uma nova Reserva", description = "Cadastra uma nova Reserva")
    @PostMapping
    public ResponseEntity<ApiResponse<ReservaDTO>> criarReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        try {
            // Tenta salvar a Reserva
            ReservaDTO savedReserva = reservaService.salvar(reservaDTO);
            
            // Retorna sucesso com o ReservaDTO salvo
            ApiResponse<ReservaDTO> response = new ApiResponse<>(savedReserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Cria um erro com a mensagem específica
            ErrorResponse errorResponse = new ErrorResponse("Argumento inválido", e.getMessage());
            ApiResponse<ReservaDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Cria um erro genérico
            ErrorResponse errorResponse = new ErrorResponse("Erro interno", e.getMessage());
            ApiResponse<ReservaDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Lista todas as reservas", description = "Retorna uma lista com todas as reservas")
    @GetMapping
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        List<ReservaDTO> reserva = reservaService.listarTodos();
        return ResponseEntity.ok(reserva);
    }
    
    @Operation(summary = "Busca reserva por ID Especifico", description = "Obtém informações de uma reserva específica pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDTO> buscarPorId(@PathVariable Long id) {
        Optional<ReservaDTO> reservaDTO = reservaService.buscarPorId(id);
        return reservaDTO.map(ResponseEntity::ok)
                             .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Lista reservas de um cliente", description = "Lista todas as reservas de um cliente especifico")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<ReservaDTO>> ReservasDoCliente(@PathVariable Long clienteId) {
        List<ReservaDTO> reservaDTO = reservaService.buscarReservasPorCliente(clienteId);
        
        if(reservaDTO.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(reservaDTO);
    }
    
    @Operation(summary = "Atualiza Status da Reserva", description = "Atualiza o status de uma reserva para Confirmada, Cancelada ou Concluída.")    
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservaDTO> atualizaStatus(@PathVariable Long id, @RequestBody ReservaStatusRequest request) {
        ReservaDTO statusAtualizado = reservaService.atualizaStatus(id, request.status());
        return ResponseEntity.ok(statusAtualizado);
    }

    @Operation(summary = "Deleta/Cancela uma reserva", description = "Cancela uma reserva existente.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarReserva(@PathVariable Long id) {
        reservaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
