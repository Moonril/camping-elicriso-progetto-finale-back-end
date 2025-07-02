package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClienteDto {

    @NotBlank(message = "Il campo 'nome' non può essere vuoto")
    private String nome;
    @NotBlank(message = "Il campo 'cognome' non può essere vuoto")
    private String cognome;
    @Email(message = "L'indirizzo email non è valido")
    @NotBlank(message = "Il campo 'email' non può essere vuoto")
    private String email;
    @NotNull(message = "Il campo 'numTelefono' non può essere vuoto")
    @Pattern(
            regexp = "^\\+?[0-9]{6,15}$",
            message = "Il numero di telefono deve contenere solo cifre e può iniziare con +"
    )
    private String numTelefono;

}
