package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "customers", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_sequence", initialValue = 100, allocationSize = 1)
    private Integer id;

    private String name;
    private String surname;
    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Booking> bookings = new ArrayList<>();

}
