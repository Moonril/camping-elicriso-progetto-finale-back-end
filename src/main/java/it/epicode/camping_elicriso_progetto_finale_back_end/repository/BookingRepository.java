package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer>,
        PagingAndSortingRepository<Booking, Integer> {
    public List<Booking> findByBookingStatus(BookingStatus bookingStatus);

    @Query("""
    SELECT r FROM Booking r
    JOIN r.accommodations a
    WHERE a.id = :accommodationId
      AND r.checkOutDate > :checkIn
      AND r.checkInDate < :checkOut
""")
    List<Booking> findOverlappingBookings(
            @Param("accommodationId") int accommodationId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
}
