package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Plot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PlotRepository extends JpaRepository<Plot, Integer>,
        PagingAndSortingRepository<Plot, Integer> {
    @Query("""
  SELECT p FROM Plot p
  WHERE p.maxNumberOfPeople >= :guests
    AND p.id NOT IN (
      SELECT acc.id FROM Booking b
      JOIN b.accommodations acc
      WHERE b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate
    )
""")
    List<Plot> findAvailablePlots(
            @Param("guests") int guests,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );

}
