package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Mesa;

import java.util.Optional;

@Repository
public interface IMesaRepository extends JpaRepository<Mesa, Long>{
}


