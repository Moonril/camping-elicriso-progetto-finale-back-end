package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "clienti")
public class Cliente {
    @Id
    @GeneratedValue
    private long id;

    private String nome;
    private String cognome;
    private String email;
    private String numTelefono;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Prenotazione> prenotazioni = new ArrayList<>();

}
