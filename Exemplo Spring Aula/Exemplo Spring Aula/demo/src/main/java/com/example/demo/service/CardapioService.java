package com.example.demo.service;

import com.example.demo.Entities.Cardapio;
import com.example.demo.Enums.StatusItem;
import com.example.demo.dto.CardapioDTO;
import com.example.demo.mapper.CardapioMapper;
import com.example.demo.repository.ICardapioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardapioService {

    @Autowired
    private ICardapioRepository cardapioRepository;

    public CardapioDTO adicionar(CardapioDTO dto) {

        Cardapio item = CardapioMapper.toEntity(dto);

        if (item.getEstoque() == 0) {
            item.setStatus(StatusItem.INDISPONIVEL);
        } else {
            item.setStatus(StatusItem.DISPONIVEL);
        }

        Cardapio salvo = cardapioRepository.save(item);

        return CardapioMapper.toDTO(salvo);
    }

    public List<CardapioDTO> listarTodos() {

        return cardapioRepository.findAll().stream()
                .map(CardapioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<CardapioDTO> buscarPorId(Long id) {

        return cardapioRepository.findById(id)
                .map(CardapioMapper::toDTO);
    }

    public CardapioDTO atualizar(Long id, CardapioDTO dto) {

        return cardapioRepository.findById(id)
                .map(item -> {

                    item.setNome(dto.getNome());
                    item.setDescricao(dto.getDescricao());
                    item.setPreco(dto.getPreco());
                    item.setEstoque(dto.getEstoque());

                    if (item.getEstoque() == 0) {
                        item.setStatus(StatusItem.INDISPONIVEL);
                    } else {
                        item.setStatus(StatusItem.DISPONIVEL);
                    }

                    Cardapio atualizado =
                            cardapioRepository.save(item);

                    return CardapioMapper.toDTO(atualizado);
                })
                .orElseThrow(() ->
                        new IllegalArgumentException("Item nao encontrado"));
    }

    public void deletar(Long id) {

        Cardapio item = cardapioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Item nao encontrado"));

        item.setStatus(StatusItem.INDISPONIVEL);

        cardapioRepository.save(item);
    }
}