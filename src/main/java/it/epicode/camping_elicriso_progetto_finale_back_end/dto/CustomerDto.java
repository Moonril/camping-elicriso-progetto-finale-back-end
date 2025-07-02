package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CustomerDto {

    @NotBlank(message = "The name field cannot be empty")
    private String name;
    @NotBlank(message = "The surname field cannot be empty")
    private String surname;
    @Email(message = "Email must have a valid, es: indirizzo@gmail.com")
    @NotBlank(message = "The email field cannot be empty")
    private String email;
    @NotNull(message = "The phoneNumber field cannot be empty")
    @Pattern(
            regexp = "^\\+?[0-9]{6,15}$",
            message = "The phoneNumber field must contain only numbers and it can start with +"
    )
    private String phoneNumber;

}
