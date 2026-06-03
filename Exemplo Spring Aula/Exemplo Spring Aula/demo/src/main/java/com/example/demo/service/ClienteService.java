package com.example.demo.service;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Reserva;
import com.example.demo.Enums.StatusCliente;
import com.example.demo.Enums.StatusReserva;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.repository.IClienteRepository;
import com.example.demo.repository.IReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private IClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private IReservaRepository reservaRepository;

    public List<ClienteDTO> listarTodos() {
        return clienteMapper.toDTOList(clienteRepository.findAll());
    }

    public Optional<ClienteDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(clienteMapper::toDTO);
    }

    public ClienteDTO salvar(ClienteDTO clienteDTO) {

        if (clienteDTO.getNome() == null || clienteDTO.getNome().trim().isEmpty() || clienteDTO.getNome().equals("string")) {
            throw new IllegalArgumentException("O nome é obrigatório e não pode ser 'string'.");
        }
        if (clienteDTO.getNome().length() > 100) {
            throw new IllegalArgumentException("O nome deve ter no máximo 100 caracteres.");
        }

        if (clienteDTO.getEmail() == null || clienteDTO.getEmail().trim().isEmpty() || clienteDTO.getEmail().equals("string")) {
            throw new IllegalArgumentException("O e-mail é obrigatório e não pode ser 'string'.");
        }
        if (clienteDTO.getEmail().length() > 100) {
            throw new IllegalArgumentException("O e-mail deve ter no máximo 100 caracteres.");
        }

        if (clienteDTO.getTelefone() == null || clienteDTO.getTelefone().trim().isEmpty() || clienteDTO.getTelefone().equals("string")) {
            throw new IllegalArgumentException("O telefone é obrigatório e não pode ser 'string'.");
        }
        if (clienteDTO.getTelefone().length() > 13) {
            throw new IllegalArgumentException("O telefone deve ter no máximo 13 caracteres.");
        }

        if (clienteDTO.getId() != null && clienteDTO.getId() != 0 && clienteRepository.existsById(clienteDTO.getId())) {
            throw new IllegalArgumentException("O ID informado já está em uso.");
        }
        
        if (clienteDTO.getStatus() == null) {
            clienteDTO.setStatus(StatusCliente.ATIVO);
        }
        
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        return clienteMapper.toDTO(clienteRepository.save(cliente));
    }

    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID informado."));
        
        List<Reserva> reservasDoCliente = reservaRepository.findByClienteId(id);
        
        boolean possuiReservaAtiva = reservasDoCliente.stream()
                .anyMatch(reserva -> reserva.getStatus() != StatusReserva.CONCLUIDA 
                                  && reserva.getStatus() != StatusReserva.CANCELADA);
        
        if (possuiReservaAtiva) {
            throw new IllegalArgumentException("Não é permitido excluir um cliente com reservas pendentes ou ativas.");
        }
        
        cliente.setStatus(StatusCliente.INATIVO);
        clienteRepository.save(cliente);
    }

    public ClienteDTO atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID informado."));

        if (clienteDTO.getId() != null && clienteDTO.getId() != 0 && !clienteDTO.getId().equals(id)) {
            throw new IllegalArgumentException("Não é permitido alterar o ID de um cliente cadastrado.");
        }

        boolean houveAlteracao = false;

        if (clienteDTO.getNome() != null && !clienteDTO.getNome().trim().isEmpty() && !clienteDTO.getNome().equals("string")) {
            if (clienteDTO.getNome().length() > 100) {
                throw new IllegalArgumentException("O nome deve ter no máximo 100 caracteres.");
            }
            clienteExistente.setNome(clienteDTO.getNome());
            houveAlteracao = true;
        }
        
        if (clienteDTO.getEmail() != null && !clienteDTO.getEmail().trim().isEmpty() && !clienteDTO.getEmail().equals("string")) {
            if (clienteDTO.getEmail().length() > 100) {
                throw new IllegalArgumentException("O e-mail deve ter no máximo 100 caracteres.");
            }
            clienteExistente.setEmail(clienteDTO.getEmail());
            houveAlteracao = true;
        }
        
        if (clienteDTO.getTelefone() != null && !clienteDTO.getTelefone().trim().isEmpty() && !clienteDTO.getTelefone().equals("string")) {
            if (clienteDTO.getTelefone().length() > 13) {
                throw new IllegalArgumentException("O telefone deve ter no máximo 13 caracteres.");
            }
            clienteExistente.setTelefone(clienteDTO.getTelefone());
            houveAlteracao = true;
        }

        if (clienteDTO.getStatus() != null) {
            clienteExistente.setStatus(clienteDTO.getStatus());
        } else if (houveAlteracao) {
            clienteExistente.setStatus(StatusCliente.ATIVO);
        }

        return clienteMapper.toDTO(clienteRepository.save(clienteExistente));
    }
}