package com.example.demo.service;

import com.example.demo.Entities.Reserva;
import com.example.demo.Enums.StatusReserva;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.mapper.ReservaMapper;
import com.example.demo.repository.IReservaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private ReservaMapper reservaMapper;
    

    public List<ReservaDTO> listarTodos() {
        return reservaMapper.toDTOList(reservaRepository.findAll());
    }

    public Optional<ReservaDTO> buscarPorId(Long id) {
        return reservaRepository.findById(id).map(reservaMapper::toDTO);
    }

    public ReservaDTO salvar(ReservaDTO reservaDTO) {
        Reserva reserva = reservaMapper.toEntity(reservaDTO);
        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }

    public void deletar(Long id) {
        reservaRepository.deleteById(id);

    }

    @Transactional
    public ReservaDTO atualizaStatus(Long id, StatusReserva novoStatus) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("reserva não encontrada utilizando id: " + id));

        reserva.setStatus(novoStatus);
        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }

    public List<ReservaDTO> buscarReservasPorCliente(Long cliente_Id){
        List<Reserva> reservas = reservaRepository.findByClienteId(cliente_Id);
        
        return reservaMapper.toDTOList(reservas);
    }
}