package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginDto {
    @NotEmpty(message = "Il campo 'username' non può essere vuoto")
    private String username;
    @NotEmpty(message = "Il campo 'password' non può essere vuoto")
    private String password;
}
