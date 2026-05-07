package com.example.demo.service;


import com.example.demo.Entities.Usuario;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;
    

    public List<UsuarioDTO> listarTodos() {
        return usuarioMapper.toDTOList(usuarioRepository.findAll());
    }

    public Optional<UsuarioDTO> buscarPorId(@NonNull Long id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toDTO);
    }

    public UsuarioDTO salvar(@NonNull UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        Usuario savedUsuario = usuarioRepository.save(usuario);
        if (savedUsuario == null) {
            throw new RuntimeException("Erro ao salvar usuário");
        }
        return usuarioMapper.toDTO(savedUsuario);
    }

    public void deletar(@NonNull Long id) {
        usuarioRepository.deleteById(id);
    }
}

