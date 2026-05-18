package com.example.demo.repository;

import java.lang.foreign.Linker.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entities.Cardapio;
import java.util.List;
import java.util.Optional;


public interface ICardapioRepository extends JpaRepository<Cardapio, Long> {

    Optional <Cardapio> findById (long id_cardapio);
}