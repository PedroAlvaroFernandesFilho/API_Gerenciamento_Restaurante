package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Mesa;

import java.util.Optional;
import java.util.List;
import com.example.demo.Enums.StatusMesa;

@Repository
public interface IMesaRepository extends JpaRepository<Mesa, Long>{

    Optional<Mesa> findById(Long id);
    
    List<Mesa> findByStatus(StatusMesa status);
}
