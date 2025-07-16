package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Glamping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GlampingRepository extends JpaRepository<Glamping, Integer>,
        PagingAndSortingRepository<Glamping, Integer> {
    @Query("""
  SELECT g FROM Glamping g
  WHERE g.maxNumberOfPeople >= :guests
    AND g.id NOT IN (
      SELECT acc.id FROM Booking b
      JOIN b.accommodations acc
      WHERE b.checkInDate < :checkOutDate AND b.checkOutDate > :checkInDate
    )
""")
    List<Glamping> findAvailableGlampings(
            @Param("guests") int guests,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate
    );
}
