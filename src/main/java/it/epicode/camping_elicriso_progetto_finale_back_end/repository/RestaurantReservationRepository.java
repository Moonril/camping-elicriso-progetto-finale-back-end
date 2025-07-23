package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.MobileHome;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.RestaurantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestaurantReservationRepository extends JpaRepository<RestaurantReservation, Integer>,
        PagingAndSortingRepository<RestaurantReservation, Integer> {
    @Query("SELECT c FROM RestaurantReservation c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<RestaurantReservation> findByNameContainingIgnoreCase(@Param("name") String name);
}
