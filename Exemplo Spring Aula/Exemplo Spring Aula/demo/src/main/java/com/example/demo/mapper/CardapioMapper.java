package com.example.demo.mapper;

import com.example.demo.Entities.Cardapio;
import com.example.demo.dto.CardapioDTO;

public class CardapioMapper {

    public static Cardapio toEntity(CardapioDTO dto) {

        Cardapio cardapio = new Cardapio();

        cardapio.setNome(dto.getNome());
        cardapio.setDescricao(dto.getDescricao());
        cardapio.setPreco(dto.getPreco());

        return cardapio;
    }

    public static CardapioDTO toDTO(Cardapio entity) {

        CardapioDTO dto = new CardapioDTO();

        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setPreco(entity.getPreco());

        return dto;
    }
}