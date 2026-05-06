package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.service.ClienteService;
import com.example.demo.service.Utils.ApiResponse;
import com.example.demo.service.Utils.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Clietes", description = "Endpoints para gerenciamento de clientes")
@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista com todos os clientes cadastrados")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> usuarios = clienteService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Busca um cliente por ID", description = "Retorna os detalhes de um cliente específico")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        Optional<ClienteDTO> clienteDTO = clienteService.buscarPorId(id);
        return clienteDTO.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo cliente", description = "Cadastra um novo cliente no sistema")
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteDTO>> criarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        try {
            // Tenta salvar o usuário
            ClienteDTO savedCliente = clienteService.salvar(clienteDTO);
            
            // Retorna sucesso com o UsuarioDTO salvo
            ApiResponse<ClienteDTO> response = new ApiResponse<>(savedCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Cria um erro com a mensagem específica
            ErrorResponse errorResponse = new ErrorResponse("Argumento inválido", e.getMessage());
            ApiResponse<ClienteDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Cria um erro genérico
            ErrorResponse errorResponse = new ErrorResponse("Erro interno", e.getMessage());
            ApiResponse<ClienteDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Deleta um cliente", description = "Remove um cliente do sistema pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}