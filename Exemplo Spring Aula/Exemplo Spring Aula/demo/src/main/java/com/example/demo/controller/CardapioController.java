package com.example.demo.controller;

import com.example.demo.Entities.Cardapio;
import com.example.demo.dto.CardapioDTO;
import com.example.demo.repository.ICardapioRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itens")
public class CardapioController {

    private final ICardapioRepository repository;

    public CardapioController(ICardapioRepository repository) {
        this.repository = repository;
    }


    @PostMapping
    public Cardapio adicionar(@Valid @RequestBody CardapioDTO dto) {

        Cardapio item = new Cardapio();

        item.setNome(dto.getNome());
        item.setDescricao(dto.getDescricao());
        item.setPreco(dto.getPreco());

        return repository.save(item);
    }

    @GetMapping
    public List<Cardapio> listar() {

        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Cardapio buscarPorId(@PathVariable Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item nao encontrado"));
    }

    @PutMapping("/{id}")
    public Cardapio atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CardapioDTO dto) {

        return repository.findById(id)
                .map(item -> {

                    item.setNome(dto.getNome());
                    item.setDescricao(dto.getDescricao());
                    item.setPreco(dto.getPreco());

                    return repository.save(item);
                })
                .orElseThrow(() -> new RuntimeException("Item nao encontrado"));
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {

        repository.deleteById(id);
    }
}
