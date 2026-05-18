package com.example.demo.service;

import com.example.demo.Entities.Cardapio;
import com.example.demo.dto.CardapioDTO;
import com.example.demo.mapper.CardapioMapper;
import com.example.demo.repository.ICardapioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardapioService {

    private final ICardapioRepository repository;

    public CardapioService(ICardapioRepository repository) {
        this.repository = repository;
    }

    
    public Cardapio adicionar(CardapioDTO dto) {

        Cardapio item = CardapioMapper.toEntity(dto);

        return repository.save(item);
    }

    
    public List<Cardapio> listar() {

        return repository.findAll();
    }

   
    public Cardapio buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item nao encontrado"));
    }

    public Cardapio atualizar(Long id, CardapioDTO dto) {

        return repository.findById(id)
                .map(item -> {

                    item.setNome(dto.getNome());
                    item.setDescricao(dto.getDescricao());
                    item.setPreco(dto.getPreco());

                    return repository.save(item);
                })
                .orElseThrow(() -> new RuntimeException("Item nao encontrado"));
    }

   
    public void deletar(Long id) {

        repository.deleteById(id);
    }
}