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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_seq")
    @SequenceGenerator(name = "restaurant_seq", sequenceName = "restaurant_sequence", initialValue = 100, allocationSize = 1)
    private long id;

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
