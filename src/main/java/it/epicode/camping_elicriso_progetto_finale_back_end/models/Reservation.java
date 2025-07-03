package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue
    private int id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime reservationCreationDate;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    private int numberOfCustomers;
    private String preference;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.PENDING;



    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @JsonIgnore
    @OneToMany(mappedBy = "reservation")
    private List<Accommodation> accommodations;
}
