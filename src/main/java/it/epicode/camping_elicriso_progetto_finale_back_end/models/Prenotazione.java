package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.StatoPrenotazione;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prenotazioni")
public class Prenotazione {
    @Id
    @GeneratedValue
    private long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCreazionePrenotazione;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;

    private int numDiPersone;
    private String preferenze;
    @Enumerated(EnumType.STRING)
    private StatoPrenotazione statoPrenotazione = StatoPrenotazione.IN_ATTESA;



    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    //connessione con alloggio
}
