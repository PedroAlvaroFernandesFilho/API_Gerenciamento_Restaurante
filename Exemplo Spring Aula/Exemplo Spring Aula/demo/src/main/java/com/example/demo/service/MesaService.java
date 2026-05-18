package com.example.demo.service;

import com.example.demo.Entities.Mesa;
import com.example.demo.Enums.StatusMesa;
import com.example.demo.dto.MesaDTO;
import com.example.demo.mapper.MesaMapper;
import com.example.demo.repository.IMesaRepository;

import jakarta.transaction.Transactional;

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

    public Optional<MesaDTO> buscarPorId(Long id) {
        return mesaRepository.findById(id).map(mesaMapper::toDTO);
    }

    public MesaDTO salvar(MesaDTO mesaDTO) {
        Mesa mesa = mesaMapper.toEntity(mesaDTO);
        return mesaMapper.toDTO(mesaRepository.save(mesa));
    }

    public void deletar(Long id) {
        mesaRepository.deleteById(id);
    }

    public Optional<MesaDTO> buscarPorStatus(StatusMesa status) {
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorStatus'");
    }

    public MesaDTO atualizarMesa(Long id, MesaDTO mesaDTO){
        Mesa mesa = mesaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mesa não encontrada utilizando id: " + id));
        
        mesa.setNumero_mesa(mesaDTO.getNumero_mesa());
        mesa.setCapacidade(mesaDTO.getCapacidade());

        return mesaMapper.toDTO(mesaRepository.save(mesa));

    }

    @Transactional
    public MesaDTO atualizarStatus(long id, StatusMesa novoStatus) {
        Mesa mesa = mesaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mesa não encontrada utilizando id: " + id));

        mesa.setStatus(novoStatus);
        return mesaMapper.toDTO(mesaRepository.save(mesa));
    }
}
