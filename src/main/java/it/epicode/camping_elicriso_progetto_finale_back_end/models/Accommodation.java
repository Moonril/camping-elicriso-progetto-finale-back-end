package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.AccomodationStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Accommodation {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private int maxNumberOfPeople;
    private String dimentions;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private AccomodationStatus accomodationStatus = AccomodationStatus.AVAILABLE;

//    @ManyToOne
//    @JoinColumn(name = "reservation_id")
//    private Reservation reservation;
    @ManyToMany(mappedBy = "accommodations")
    private Set<Booking> bookings;
}
