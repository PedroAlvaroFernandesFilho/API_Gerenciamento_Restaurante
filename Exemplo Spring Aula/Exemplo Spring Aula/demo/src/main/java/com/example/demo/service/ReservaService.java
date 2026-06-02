package com.example.demo.service;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Mesa;
import com.example.demo.Entities.Reserva;
import com.example.demo.Enums.StatusCliente;
import com.example.demo.Enums.StatusMesa;
import com.example.demo.Enums.StatusPedido;
import com.example.demo.Enums.StatusReserva;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.mapper.ReservaMapper;
import com.example.demo.repository.IClienteRepository;
import com.example.demo.repository.IMesaRepository;
import com.example.demo.repository.IPedidoRepository;
import com.example.demo.repository.IReservaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private ReservaMapper reservaMapper;

    @Autowired
    private IPedidoRepository pedidoRepository;

    @Autowired
    private IClienteRepository clienteRepository;
    
    @Autowired
    private IMesaRepository mesaRepository;


    public List<ReservaDTO> listarTodos() {
        return reservaMapper.toDTOList(reservaRepository.findAll());
    }

    public Optional<ReservaDTO> buscarPorId(Long id) {
        return reservaRepository.findById(id).map(reservaMapper::toDTO);
    }

    @Transactional
    public ReservaDTO salvar(ReservaDTO reservaDTO) {
        
        if (reservaDTO.getId() != null && reservaRepository.existsById(reservaDTO.getId())) {
            throw new IllegalArgumentException("O ID já está cadastrado.");
        }

        Cliente cliente = clienteRepository.findById(reservaDTO.getCliente_Id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
        
        if (cliente.getStatus() == StatusCliente.INATIVO) { 
            throw new IllegalArgumentException("O cliente está inativo.");
        }

        Mesa mesa = mesaRepository.findById(reservaDTO.getMesa_Id())
                .orElseThrow(() -> new IllegalArgumentException("Mesa não encontrada."));
        
        if (mesa.getStatus() != StatusMesa.LIVRE) { 
            throw new IllegalArgumentException("A mesa está indisponível.");
        }

        LocalDate dataInput = reservaDTO.getDataReserva();
        if (dataInput == null) {
            throw new IllegalArgumentException("A data é inválida.");
        }
        
        LocalDate hoje = LocalDate.now();
        if (dataInput.isBefore(hoje)) {
            throw new IllegalArgumentException("A data não pode ser anterior à atual.");
        }
        
        if (dataInput.isAfter(hoje.plusYears(1))) {
            throw new IllegalArgumentException("Não é permitido reservas maiores que 1 ano de diferença.");
        }

        LocalTime horaInput = reservaDTO.getHoraReserva();
        if (horaInput == null) {
            throw new IllegalArgumentException("O horário é inválido.");
        }
        
        // Se a reserva for para o dia de hoje, valida se a hora não ficou no passado
        if (dataInput.isEqual(hoje) && horaInput.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("O horário é inválido.");
        }
        
        // padronizando o status
        String statusInput = reservaDTO.getStatus();
        
        // Nota: Sua classe 'StatusReserva.java' usa o termo feminino CONFIRMADA
        if (statusInput == null || statusInput.trim().isEmpty() || statusInput.equalsIgnoreCase("confirmado") || statusInput.equalsIgnoreCase("confirmada")) {
            reservaDTO.setStatus(StatusReserva.CONFIRMADA.name());
        } else {
            throw new IllegalArgumentException("Status inválido.");
        }

        Reserva reserva = reservaMapper.toEntity(reservaDTO);
        return reservaMapper.toDTO(reservaRepository.save(reserva));
        }


    @Transactional
    public ReservaDTO deletar(Long id) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada."));

        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            return reservaMapper.toDTO(reserva);
        }

        if (reserva.getStatus() == StatusReserva.CONCLUIDA) {
            throw new IllegalStateException("Não é permitido cancelar uma reserva que ja foi concluída.");
        }

        boolean possuiPedidosConfirmados = pedidoRepository.existsByReservaIdAndStatus(id, StatusPedido.CONFIRMADO);

        if (possuiPedidosConfirmados) {
            throw new IllegalStateException("Existem pedidos confirmados para essa reserva.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        return reservaMapper.toDTO(reservaRepository.save(reserva));    
    }

    @Transactional
    public ReservaDTO atualizaStatus(Long id, StatusReserva novoStatus) {
        Reserva reserva = reservaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("reserva não encontrada utilizando id: " + id));

        if (reserva.getStatus() == novoStatus){
            return reservaMapper.toDTO(reserva);
        }

        if (reserva.getStatus() == StatusReserva.CONCLUIDA || reserva.getStatus() == StatusReserva.CANCELADA){
            throw new IllegalStateException("Não é permitido alterar o status de uma reserva que já está " + reserva.getStatus() + ".");
        }

        reserva.setStatus(novoStatus);
        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }

    public List<ReservaDTO> buscarReservasPorCliente(Long cliente_Id){
        List<Reserva> reservas = reservaRepository.findByClienteId(cliente_Id);
        
        return reservaMapper.toDTOList(reservas);
    }
}