package com.example.demo.service;

import com.example.demo.Entities.Mesa;
import com.example.demo.dto.MesaDTO;
import com.example.demo.mapper.MesaMapper;
import com.example.demo.repository.IMesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MesaService {

    @Autowired
    private IMesaRepository mesaRepository;

    @Autowired
    private MesaMapper mesaMapper;
    

    public List<MesaDTO> listarTodos() {
        return mesaMapper.toDTOList(mesaRepository.findAll());
    }

    public Optional<MesaDTO> buscarPorId(Long id_mesa) {
        return mesaRepository.findById(id_mesa).map(mesaMapper::toDTO);
    }

    public MesaDTO salvar(MesaDTO mesaDTO) {
        Mesa mesa = mesaMapper.toEntity(mesaDTO);
        return mesaMapper.toDTO(mesaRepository.save(mesa));
    }

    public void deletar(Long id_mesa) {
        mesaRepository.deleteById(id_mesa);
    }
}
