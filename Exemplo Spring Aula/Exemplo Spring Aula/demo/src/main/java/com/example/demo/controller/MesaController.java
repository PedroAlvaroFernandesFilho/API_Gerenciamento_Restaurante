package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.MesaDTO;
import com.example.demo.service.MesaService;
import com.example.demo.service.Utils.ApiResponse;
import com.example.demo.service.Utils.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Mesas", description = "Endpoints para gerenciamento de Mesas")
@RestController
@RequestMapping("api/mesas")
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @Operation(summary = "Lista todas as mesas", description = "Retorna uma lista com todas as mesas cadastradas")
    @GetMapping
    public ResponseEntity<List<MesaDTO>> listarMesas() {
        List<MesaDTO> mesa = mesaService.listarTodos();
        return ResponseEntity.ok(mesa);
    }

    @Operation(summary = "Busca Mesa por ID Especifico", description = "Retorna os detalhes de uma mesa específica")
    @GetMapping("/{id}")
    public ResponseEntity<MesaDTO> buscarPorId(@PathVariable Long id) {
        Optional<MesaDTO> mesaDTO = mesaService.buscarPorId(id);
        return mesaDTO.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria uma nova Mesa", description = "Cadastra uma nova Mesa no sistema")
    @PostMapping
    public ResponseEntity<ApiResponse<MesaDTO>> criarUsuario(@Valid @RequestBody MesaDTO mesaDTO) {
        try {
            // Tenta salvar a Mesa
            MesaDTO savedMesa = mesaService.salvar(mesaDTO);
            
            // Retorna sucesso com o MesaDTO salvo
            ApiResponse<MesaDTO> response = new ApiResponse<>(savedMesa);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Cria um erro com a mensagem específica
            ErrorResponse errorResponse = new ErrorResponse("Argumento inválido", e.getMessage());
            ApiResponse<MesaDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Cria um erro genérico
            ErrorResponse errorResponse = new ErrorResponse("Erro interno", e.getMessage());
            ApiResponse<MesaDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Deleta uma Mesa", description = "Remove uma Mesa do sistema pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMesa(@PathVariable Long id) {
        mesaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
