package com.example.demo.mapper;

import com.example.demo.Entities.Cardapio;
import com.example.demo.dto.CardapioDTO;

public class CardapioMapper {

    public static Cardapio toEntity(CardapioDTO dto) {

        Cardapio cardapio = new Cardapio();

        cardapio.setId(dto.getId());
        cardapio.setNome(dto.getNome());
        cardapio.setDescricao(dto.getDescricao());
        cardapio.setPreco(dto.getPreco());
        cardapio.setEstoque(dto.getEstoque());
        cardapio.setStatus(dto.getStatus());

        return cardapio;
    }

    public static CardapioDTO toDTO(Cardapio entity) {

        CardapioDTO dto = new CardapioDTO();

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setPreco(entity.getPreco());
        dto.setEstoque(entity.getEstoque());
        dto.setStatus(entity.getStatus());

        return dto;
    }
}