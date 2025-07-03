package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Reservation;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>,
        PagingAndSortingRepository<Reservation, Integer> {
    public List<Reservation> findByBookingStatus(BookingStatus bookingStatus);
}
