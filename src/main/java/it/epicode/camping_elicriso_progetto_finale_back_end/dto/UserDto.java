package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty(message = "The name field cannot be empty")
    private String name;
    @NotEmpty(message = "The surname field cannot be empty")
    private String surname;
    @Email(message = "Email must have a valid format, es: indirizzo@gmail.com")
    private String email;
    @NotEmpty(message = "The username field cannot be empty")
    private String username;
    @NotEmpty(message = "The password field cannot be empty")
    private String password; // todo buondaries password
}
