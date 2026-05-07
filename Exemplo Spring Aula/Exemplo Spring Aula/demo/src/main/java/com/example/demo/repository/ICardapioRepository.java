package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entities.Cardapio;

public interface ICardapioRepository extends JpaRepository<Cardapio, Long> {
}
