package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Reservation> reservations = new ArrayList<>();

}
