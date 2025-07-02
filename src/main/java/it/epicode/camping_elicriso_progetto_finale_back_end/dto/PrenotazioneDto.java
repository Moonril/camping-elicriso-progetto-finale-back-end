package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.StatoPrenotazione;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PrenotazioneDto {


    @NotNull(message = "Il campo 'dataCheckIn' non può essere vuoto")
    @Future(message = "La data di check-in deve essere nel futuro")
    private LocalDate dataCheckIn;
    @NotNull(message = "Il campo 'dataCheckOut' non può essere vuoto")
    @Future(message = "La data di check-out deve essere nel futuro")
    private LocalDate dataCheckOut;

    @AssertTrue(message = "La data di check-out deve essere successiva al check-in")
    public boolean isIntervalloDateValido() {
        return dataCheckIn != null && dataCheckOut != null && dataCheckIn.isBefore(dataCheckOut);
    }

    @NotNull(message = "Il campo 'numDiPersone' non può essere vuoto")
    @Size(min = 1, max = 6)// numero di persone massimo, tanti quanti permette l'alloggio
    private int numDiPersone;
    private String preferenze;

    @NotNull(message = "Il campo 'clienteId' non può essere vuoto")
    private long clienteId;
}
