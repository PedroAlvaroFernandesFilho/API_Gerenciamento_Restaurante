package com.example.demo.service;

import com.example.demo.Entities.Mesa;
import com.example.demo.Enums.StatusMesa;
import com.example.demo.Enums.StatusReserva;
import com.example.demo.dto.MesaDTO;
import com.example.demo.mapper.MesaMapper;
import com.example.demo.repository.IMesaRepository;
import com.example.demo.repository.IReservaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    @Autowired
    private IMesaRepository mesaRepository;

    @Autowired
    private MesaMapper mesaMapper;
    
    @Autowired
    private IReservaRepository reservaRepository;

    // metodo para validar os limites dos dados
    private void validarLimitesDadosMesa(Integer numeroMesa, Integer capacidade) {
        if (numeroMesa == null || numeroMesa <= 0 || numeroMesa > 100000) {
            throw new IllegalArgumentException("O número da mesa não pode ser nulo, zerado ou maior que 100000.");
        }
        if (capacidade == null || capacidade <= 0 || capacidade > 50) {
            throw new IllegalArgumentException("A capacidade da mesa não pode ser nula, zerada ou maior que 50.");
        }
    }

    public List<MesaDTO> listarTodos() {
        return mesaMapper.toDTOList(mesaRepository.findAll());
    }

    public Optional<MesaDTO> buscarPorId(Long id) {
        return mesaRepository.findById(id).map(mesaMapper::toDTO);
    }

    @Transactional
    public MesaDTO salvar(MesaDTO mesaDTO) {
        
        if (mesaDTO.getId() != null) {
            if (mesaDTO.getId() <= 0 || mesaDTO.getId() > 999999999L) {
                throw new IllegalArgumentException("O ID informado é inválido ou quebra o limite permitido.");
            }
            if (mesaRepository.existsById(mesaDTO.getId())) {
                throw new IllegalArgumentException("O ID informado já está cadastrado.");
            }
        }

        validarLimitesDadosMesa(mesaDTO.getNumero_mesa(), mesaDTO.getCapacidade());

        String statusInput = mesaDTO.getStatus();
        if (statusInput == null || statusInput.trim().isEmpty() || statusInput.equalsIgnoreCase("livre")) {
            mesaDTO.setStatus(StatusMesa.LIVRE.name());
        } else {
            throw new IllegalArgumentException("Status inválido para o cadastro inicial da mesa.");
        }

        Mesa mesa = mesaMapper.toEntity(mesaDTO);
        return mesaMapper.toDTO(mesaRepository.save(mesa));
    }


    public void deletar(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("Mesa não encontrada utilizando id: " + id));       
        long reservasAtivas = reservaRepository.countReservasAtivas(
            id, StatusReserva.CONCLUIDA, StatusReserva.CANCELADA);

        if (reservasAtivas > 0) {
            throw new RuntimeException("Não é possivel inativar a mesa. Ela possui " + reservasAtivas + "reservas em aberto.");
        }
        mesa.setStatus(StatusMesa.INATIVA);
        mesaRepository.save(mesa);
    }

    public List<MesaDTO> buscarPorStatus(StatusMesa status) {
        List<Mesa> mesaStatusOptional = mesaRepository.findByStatus(status);
        return mesaMapper.toDTOList(mesaStatusOptional);
    }

    public MesaDTO atualizarMesa(Long id, MesaDTO mesaDTO){
        Mesa mesa = mesaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mesa não encontrada utilizando id: " + id));
        
        validarLimitesDadosMesa(mesaDTO.getNumero_mesa(), mesaDTO.getCapacidade());

        mesa.setNumero_mesa(mesaDTO.getNumero_mesa());
        mesa.setCapacidade(mesaDTO.getCapacidade());

        return mesaMapper.toDTO(mesaRepository.save(mesa));

    }

    @Transactional
    public MesaDTO atualizarStatus(long id, StatusMesa novoStatus) {
        Mesa mesa = mesaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mesa não encontrada utilizando id: " + id));

       StatusMesa statusAtual = mesa.getStatus();

        if (statusAtual == novoStatus) {
            return mesaMapper.toDTO(mesa);
        }

        long reservasAtivas = reservaRepository.countReservasAtivas(
            id, StatusReserva.CONCLUIDA, StatusReserva.CANCELADA);

        if (statusAtual == StatusMesa.LIVRE) {
            
        } else if (statusAtual == StatusMesa.RESERVADA) {
            if (novoStatus == StatusMesa.LIVRE || novoStatus == StatusMesa.INATIVA) {
                if (reservasAtivas > 0) {
                    throw new IllegalStateException("A mesa possui reservas pendentes. O status só pode retornar para LIVRE ou INATIVA se as reservas forem Concluídas ou Canceladas.");
                }
            }
        } else if (statusAtual == StatusMesa.OCUPADA) {
            if (reservasAtivas > 0) {
                throw new IllegalStateException("Não é possível alterar o status de uma mesa OCUPADA enquanto a reserva não for concluída.");
            }
        } else if (statusAtual == StatusMesa.INATIVA) {
        
        }
        
        mesa.setStatus(novoStatus);
        return mesaMapper.toDTO(mesaRepository.save(mesa));
    }
}
