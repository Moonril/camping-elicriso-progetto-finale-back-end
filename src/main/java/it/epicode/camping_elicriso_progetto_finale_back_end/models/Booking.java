package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue
    private int id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime bookingCreationDate;
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
    @ManyToMany
    @JoinTable(
            name = "booking_accommodation",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "accommodation_id")
    )
    private Set<Accommodation> accommodations;

    @JsonIgnore
    @OneToMany(mappedBy = "booking")
    private Set<RestaurantReservation> restaurantReservations;

}
