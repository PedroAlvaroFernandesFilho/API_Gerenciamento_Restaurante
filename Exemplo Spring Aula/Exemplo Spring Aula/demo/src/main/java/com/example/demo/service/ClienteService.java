package com.example.demo.service;

import com.example.demo.Entities.Cliente;
import com.example.demo.Enums.StatusCliente;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.repository.IClienteRepository;
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

    public List<ClienteDTO> listarTodos() {
        return clienteMapper.toDTOList(clienteRepository.findAll());
    }

    public Optional<ClienteDTO> buscarPorId(Long id) {
        return clienteRepository.findById(id).map(clienteMapper::toDTO);
    }

    public ClienteDTO salvar(ClienteDTO clienteDTO) {
        // Ignora o valor padrão "string" do Swagger nas validações de criação
        if (clienteDTO.getNome() == null || clienteDTO.getNome().trim().isEmpty() || clienteDTO.getNome().equals("string")) {
            throw new IllegalArgumentException("O nome é obrigatório e não pode ser 'string'.");
        }
        if (clienteDTO.getEmail() == null || clienteDTO.getEmail().trim().isEmpty() || clienteDTO.getEmail().equals("string")) {
            throw new IllegalArgumentException("O e-mail é obrigatório e não pode ser 'string'.");
        }
        if (clienteDTO.getTelefone() == null || clienteDTO.getTelefone().trim().isEmpty() || clienteDTO.getTelefone().equals("string")) {
            throw new IllegalArgumentException("O telefone é obrigatório e não pode ser 'string'.");
        }

        // Se o Swagger enviar ID 0, tratamos como se não tivesse ID enviado
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
        
        cliente.setStatus(StatusCliente.INATIVO);
        clienteRepository.save(cliente);
    }

    public ClienteDTO atualizar(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado com o ID informado."));

        // Proteção para o Swagger que pode mandar o id como 0 por padrão
        if (clienteDTO.getId() != null && clienteDTO.getId() != 0 && !clienteDTO.getId().equals(id)) {
            throw new IllegalArgumentException("Não é permitido alterar o ID de um cliente cadastrado.");
        }

        boolean houveAlteracao = false;

        // Verifica se o campo veio preenchido, não é vazio, e NÃO é a palavra padrão "string"
        if (clienteDTO.getNome() != null && !clienteDTO.getNome().trim().isEmpty() && !clienteDTO.getNome().equals("string")) {
            clienteExistente.setNome(clienteDTO.getNome());
            houveAlteracao = true;
        }
        
        if (clienteDTO.getEmail() != null && !clienteDTO.getEmail().trim().isEmpty() && !clienteDTO.getEmail().equals("string")) {
            clienteExistente.setEmail(clienteDTO.getEmail());
            houveAlteracao = true;
        }
        
        if (clienteDTO.getTelefone() != null && !clienteDTO.getTelefone().trim().isEmpty() && !clienteDTO.getTelefone().equals("string")) {
            clienteExistente.setTelefone(clienteDTO.getTelefone());
            houveAlteracao = true;
        }

        // O status só é alterado se for enviado explicitamente algo diferente do padrão
        if (clienteDTO.getStatus() != null) {
            clienteExistente.setStatus(clienteDTO.getStatus());
        } else if (houveAlteracao) {
            // Se mudou algum dado (nome, email, telefone), volta a ficar ATIVO automaticamente
            clienteExistente.setStatus(StatusCliente.ATIVO);
        }

        return clienteMapper.toDTO(clienteRepository.save(clienteExistente));
    }
}