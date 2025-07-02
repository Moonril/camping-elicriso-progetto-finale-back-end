package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Integer>,
        PagingAndSortingRepository<Reservation, Integer> {
}
