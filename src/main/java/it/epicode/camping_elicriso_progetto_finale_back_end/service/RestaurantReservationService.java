package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.RestaurantReservationDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Booking;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.RestaurantReservation;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.BookingRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.CustomerRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.RestaurantReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantReservationService {
    @Autowired
    private RestaurantReservationRepository restaurantReservationRepository;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private BookingService bookingService;

    public RestaurantReservation saveRestaurantReservation(RestaurantReservationDto restaurantReservationDto) throws NotFoundException {
        Booking booking = bookingService.getBooking(restaurantReservationDto.getBookingId());
        RestaurantReservation restaurantReservation = new RestaurantReservation();

        restaurantReservation.setNumberOfPeople(restaurantReservationDto.getNumberOfPeople());
        restaurantReservation.setReservationDate(restaurantReservationDto.getReservationDate());
        restaurantReservation.setAdditionalNotes(restaurantReservationDto.getAdditionalNotes());
        restaurantReservation.setName(restaurantReservationDto.getName());
        restaurantReservation.setPhoneNumber(restaurantReservationDto.getPhoneNumber());
        restaurantReservation.setBooking(booking);

        return restaurantReservationRepository.save(restaurantReservation);
    }

    public Page<RestaurantReservation> getAllRestaurantReservations(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return restaurantReservationRepository.findAll(pageable);
    }

    public RestaurantReservation getRestaurantReservation(int id) throws NotFoundException{
        return restaurantReservationRepository.findById(id).orElseThrow(() -> new NotFoundException("RestaurantReservation with id: " + id + " not found"));
    }

    public List<RestaurantReservation> getReservationsByName(String name) throws NotFoundException {
        List<RestaurantReservation> restaurantReservations = restaurantReservationRepository.findByNameContainingIgnoreCase(name);

        if (restaurantReservations.isEmpty()) {
            throw new NotFoundException("Nessun cliente trovato con nome contenente: " + name);
        }

        return restaurantReservations;
    }


    public RestaurantReservation updateRestaurantReservation(int id, RestaurantReservationDto restaurantReservationDto) throws NotFoundException{
        RestaurantReservation restaurantReservationToUpdate = getRestaurantReservation(id);

        restaurantReservationToUpdate.setNumberOfPeople(restaurantReservationDto.getNumberOfPeople());
        restaurantReservationToUpdate.setReservationDate(restaurantReservationDto.getReservationDate());
        restaurantReservationToUpdate.setAdditionalNotes(restaurantReservationDto.getAdditionalNotes());
        restaurantReservationToUpdate.setName(restaurantReservationDto.getName());
        restaurantReservationToUpdate.setPhoneNumber(restaurantReservationDto.getPhoneNumber());


        if(restaurantReservationToUpdate.getBooking().getId()!=restaurantReservationDto.getBookingId()){
            Booking booking = bookingService.getBooking(restaurantReservationDto.getBookingId());
            restaurantReservationToUpdate.setBooking(booking);
        }

        return restaurantReservationRepository.save(restaurantReservationToUpdate);
    }

    public void deleteRestaurantReservation(int id) throws NotFoundException{
        RestaurantReservation restaurantReservationToDelete = getRestaurantReservation(id);

        restaurantReservationRepository.delete(restaurantReservationToDelete);
    }
}
