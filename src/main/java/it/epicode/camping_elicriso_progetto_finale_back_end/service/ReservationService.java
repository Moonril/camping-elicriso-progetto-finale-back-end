package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.ReservationDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Reservation;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.CustomerRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    public Reservation saveReservation(ReservationDto reservationDto) throws NotFoundException {
        Customer customer = customerService.getCustomer(reservationDto.getCustomerId());
        Reservation reservation = new Reservation();

        reservation.setCheckInDate(reservationDto.getCheckInDate());
        reservation.setCheckOutDate(reservationDto.getCheckOutDate());
        reservation.setNumberOfCustomers(reservationDto.getNumberOfCustomers());
        reservation.setPreference(reservationDto.getPreference());
        reservation.setCustomer(customer);

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

    public void deleteReservation(int id) throws NotFoundException{
        Reservation reservationToDelete = getReservation(id);

        reservationRepository.delete(reservationToDelete);
    }
}
