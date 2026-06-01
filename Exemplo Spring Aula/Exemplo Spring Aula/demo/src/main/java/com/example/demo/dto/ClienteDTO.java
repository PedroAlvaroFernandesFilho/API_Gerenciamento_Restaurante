package com.example.demo.dto;

import com.example.demo.Enums.StatusCliente;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClienteDTO {
    
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private StatusCliente status;
}