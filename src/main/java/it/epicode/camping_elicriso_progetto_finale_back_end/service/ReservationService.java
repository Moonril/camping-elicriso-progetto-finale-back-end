package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.ReservationDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Accommodation;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Reservation;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.CustomerRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PlotService plotService;

    @Autowired
    private GlampingService glampingService;

    @Autowired
    private MobileHomeService mobileHomeService;

    public Reservation saveReservation(ReservationDto reservationDto) throws NotFoundException {
        Customer customer = customerService.getCustomer(reservationDto.getCustomerId());

        Accommodation accommodation;
        switch (reservationDto.getAccommodationType()) {
            case PLOT:
                accommodation = plotService.getPlot(reservationDto.getAccommodationId());
                break;
            case GLAMPING:
                accommodation = glampingService.getGlamping(reservationDto.getAccommodationId());
                break;
            case MOBILEHOME:
                accommodation = mobileHomeService.getMobileHome(reservationDto.getAccommodationId());
                break;
            default:
                throw new IllegalArgumentException("Invalid accommodation type: " + reservationDto.getAccommodationType());
        }

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(reservationDto.getCheckInDate());
        reservation.setCheckOutDate(reservationDto.getCheckOutDate());
        reservation.setNumberOfCustomers(reservationDto.getNumberOfCustomers());
        reservation.setPreference(reservationDto.getPreference());
        reservation.setCustomer(customer);
        reservation.setAccommodations(Set.of(accommodation));

        List<Reservation> overlappingReservations =
                reservationRepository.findOverlappingReservations(
                        reservationDto.getAccommodationId(),
                        reservationDto.getCheckInDate(),
                        reservationDto.getCheckOutDate()
                );

        if (!overlappingReservations.isEmpty()) {
            throw new IllegalStateException("Accommodation is already reserved during this period.");
        }


        return reservationRepository.save(reservation);
    }

    public Page<Reservation> getAllReservations(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return reservationRepository.findAll(pageable);
    }

    public Reservation getReservation(int id) throws NotFoundException{
        return reservationRepository.findById(id).orElseThrow(() -> new NotFoundException("Reservation with id: " + id + " not found"));
    }


    public Reservation updateReservation(int id, ReservationDto reservationDto) throws NotFoundException{
        Reservation reservationToUpdate = getReservation(id);

        reservationToUpdate.setCheckInDate(reservationDto.getCheckInDate());
        reservationToUpdate.setCheckOutDate(reservationDto.getCheckOutDate());
        reservationToUpdate.setNumberOfCustomers(reservationDto.getNumberOfCustomers());
        reservationToUpdate.setPreference(reservationDto.getPreference());

        if(reservationToUpdate.getCustomer().getId()!=reservationDto.getCustomerId()){
            Customer customer = customerService.getCustomer(reservationDto.getCustomerId());
            reservationToUpdate.setCustomer(customer);
        }

        return reservationRepository.save(reservationToUpdate);
    }

    public Reservation patchReservation(int id, BookingStatus newStatus) throws NotFoundException, IllegalArgumentException {
        Reservation reservation = getReservation(id);
        BookingStatus currentStatus = reservation.getBookingStatus();

        Map<BookingStatus, List<BookingStatus>> validTransitions = Map.of(
                BookingStatus.PENDING, List.of(BookingStatus.CONFIRMED, BookingStatus.CANCELLED),
                BookingStatus.CONFIRMED, List.of(BookingStatus.CANCELLED, BookingStatus.COMPLETED),
                BookingStatus.CANCELLED, List.of(),
                BookingStatus.COMPLETED, List.of()
        );

        //non so se necessario
        if (!validTransitions.get(currentStatus).contains(newStatus)) {
            throw new IllegalArgumentException("Transition from " + currentStatus + " to " + newStatus + " not allowed.");
        }


        reservation.setBookingStatus(newStatus);


        return reservationRepository.save(reservation);
    }

    public void deleteReservation(int id) throws NotFoundException{
        Reservation reservationToDelete = getReservation(id);

        reservationRepository.delete(reservationToDelete);
    }

    // per cambiare il booking status dopo il check out
    public void autoCompleteReservations() {
        List<Reservation> confirmedReservations = reservationRepository.findByBookingStatus(BookingStatus.CONFIRMED);
        LocalDate today = LocalDate.now();

        for (Reservation reservation : confirmedReservations) {
            if (reservation.getCheckOutDate().isBefore(today)) {
                reservation.setBookingStatus(BookingStatus.COMPLETED);
                reservationRepository.save(reservation);
            }
        }
    }
}
