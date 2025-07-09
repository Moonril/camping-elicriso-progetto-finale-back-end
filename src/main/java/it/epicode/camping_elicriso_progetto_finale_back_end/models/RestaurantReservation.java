package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "restaurant-reservations")
public class RestaurantReservation {
    @Id
    @GeneratedValue
    private int id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime reservationCreationDate;
    private String name;
    private int numberOfPeople;
    private LocalDateTime reservationDate;
    private String additionalNotes;
    private String phoneNumber;


    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
