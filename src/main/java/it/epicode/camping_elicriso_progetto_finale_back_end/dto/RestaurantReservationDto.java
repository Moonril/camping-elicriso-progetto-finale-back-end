package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RestaurantReservationDto {

    @NotBlank(message = "The field name cannot be empty")
    private String name;
    @NotNull(message = "The field numberOfPeople cannot be empty")
    @Min(1)
    private int numberOfPeople;
    @NotNull(message = "The field checkInDate cannot be empty")
    @Future(message = "The check-in date cannot be in the past")
    private LocalDateTime reservationDate;
    private String additionalNotes;
    @NotNull(message = "The phoneNumber field cannot be empty")
    @Pattern(
            regexp = "^\\+?[0-9]{6,15}$",
            message = "The phoneNumber field must contain only numbers and it can start with +"
    )
    private String phoneNumber;



    private int bookingId;
}
