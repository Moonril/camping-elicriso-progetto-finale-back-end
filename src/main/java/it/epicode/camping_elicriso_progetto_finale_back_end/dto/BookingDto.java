package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.AccomodationType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDto {


    @NotNull(message = "The field checkInDate cannot be empty")
    @Future(message = "The check-in date cannot be in the past")
    private LocalDate checkInDate;
    @NotNull(message = "The field checkOutDate cannot be empty")
    @Future(message = "The check-out date cannot be in the past")
    private LocalDate checkOutDate;

    @AssertTrue(message = "The check-out date must be after the check-in date")
    public boolean isIntervallDateValid() {
        return checkInDate != null && checkOutDate != null && checkInDate.isBefore(checkOutDate);
    }

    @NotNull(message = "The field numberOfCustomers cannot be empty")
    @Min(1)
    @Max(6) // numero di persone massimo, tanti quanti permette l'alloggio
    private int numberOfCustomers;
    private String preference;

    @NotNull(message = "The field customerId cannot be null")
    private int customerId;

    @NotNull(message = "The filed accomodationId cannot be null")
    private int accommodationId;

    @NotNull(message = "Accommodation type is required")
    private AccomodationType accommodationType;
}
