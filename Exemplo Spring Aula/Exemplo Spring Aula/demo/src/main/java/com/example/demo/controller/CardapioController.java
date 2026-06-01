package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CardapioDTO;
import com.example.demo.service.CardapioService;
import com.example.demo.service.Utils.ApiResponse;
import com.example.demo.service.Utils.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Cardapio", description = "Endpoints para gerenciamento do Cardapio")
@RestController
@RequestMapping("api/cardapio")
public class CardapioController {

    @Autowired
    private CardapioService cardapioService;

    @Operation(summary = "Cria um novo item do Cardapio",
            description = "Cadastra um novo item no cardapio")
    @PostMapping
    public ResponseEntity<ApiResponse<CardapioDTO>> criarCardapio(
            @Valid @RequestBody CardapioDTO cardapioDTO) {

        try {

            CardapioDTO savedCardapio =
                    cardapioService.adicionar(cardapioDTO);

            ApiResponse<CardapioDTO> response =
                    new ApiResponse<>(savedCardapio);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException e) {

            ErrorResponse errorResponse =
                    new ErrorResponse("Argumento invalido", e.getMessage());

            ApiResponse<CardapioDTO> response =
                    new ApiResponse<>(errorResponse);

            return ResponseEntity
                    .badRequest()
                    .body(response);

        } catch (Exception e) {

            ErrorResponse errorResponse =
                    new ErrorResponse("Erro interno", e.getMessage());

            ApiResponse<CardapioDTO> response =
                    new ApiResponse<>(errorResponse);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @Operation(summary = "Lista todos os itens do Cardapio",
            description = "Retorna uma lista com todos os itens")
    @GetMapping
    public ResponseEntity<List<CardapioDTO>> listarCardapio() {

        List<CardapioDTO> cardapio =
                cardapioService.listarTodos();

        return ResponseEntity.ok(cardapio);
    }

    @Operation(summary = "Busca item do Cardapio por ID",
            description = "Obtém informações de um item específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<CardapioDTO> buscarPorId(@PathVariable Long id) {

        Optional<CardapioDTO> cardapioDTO =
                cardapioService.buscarPorId(id);

        return cardapioDTO
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

    @Operation(summary = "Atualiza item do Cardapio",
            description = "Atualiza um item existente")
    @PutMapping("/{id}")
    public ResponseEntity<CardapioDTO> atualizarCardapio(
            @PathVariable Long id,
            @Valid @RequestBody CardapioDTO cardapioDTO) {

        CardapioDTO cardapioAtualizado =
                cardapioService.atualizar(id, cardapioDTO);

        return ResponseEntity.ok(cardapioAtualizado);
    }

    @Operation(summary = "Remove um item do Cardapio",
            description = "Remove um item existente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCardapio(@PathVariable Long id) {

        cardapioService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}