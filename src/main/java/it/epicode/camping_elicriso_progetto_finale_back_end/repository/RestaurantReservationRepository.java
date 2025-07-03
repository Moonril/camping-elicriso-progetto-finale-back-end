package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.MobileHome;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.RestaurantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
public interface RestaurantReservationRepository extends JpaRepository<RestaurantReservation, Integer>,
        PagingAndSortingRepository<RestaurantReservation, Integer> {
}
