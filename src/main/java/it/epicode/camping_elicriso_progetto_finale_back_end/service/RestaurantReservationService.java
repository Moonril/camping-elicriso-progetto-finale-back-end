package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.RestaurantReservationDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Customer;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.RestaurantReservation;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.CustomerRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.RestaurantReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RestaurantReservationService {
    @Autowired
    private RestaurantReservationRepository restaurantReservationRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    public RestaurantReservation saveRestaurantReservation(RestaurantReservationDto restaurantReservationDto) throws NotFoundException {
        Customer customer = customerService.getCustomer(restaurantReservationDto.getCustomerId());
        RestaurantReservation restaurantReservation = new RestaurantReservation();

        restaurantReservation.setNumberOfPeople(restaurantReservationDto.getNumberOfPeople());
        restaurantReservation.setReservationDate(restaurantReservationDto.getReservationDate());
        restaurantReservation.setAdditionalNotes(restaurantReservationDto.getAdditionalNotes());
        restaurantReservation.setCustomer(customer);

        return restaurantReservationRepository.save(restaurantReservation);
    }

    public Page<RestaurantReservation> getAllRestaurantReservations(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return restaurantReservationRepository.findAll(pageable);
    }

    public RestaurantReservation getRestaurantReservation(int id) throws NotFoundException{
        return restaurantReservationRepository.findById(id).orElseThrow(() -> new NotFoundException("RestaurantReservation with id: " + id + " not found"));
    }


    public RestaurantReservation updateRestaurantReservation(int id, RestaurantReservationDto restaurantReservationDto) throws NotFoundException{
        RestaurantReservation restaurantReservationToUpdate = getRestaurantReservation(id);

        restaurantReservationToUpdate.setNumberOfPeople(restaurantReservationDto.getNumberOfPeople());
        restaurantReservationToUpdate.setReservationDate(restaurantReservationDto.getReservationDate());
        restaurantReservationToUpdate.setAdditionalNotes(restaurantReservationDto.getAdditionalNotes());

        if(restaurantReservationToUpdate.getCustomer().getId()!=restaurantReservationDto.getCustomerId()){
            Customer customer = customerService.getCustomer(restaurantReservationDto.getCustomerId());
            restaurantReservationToUpdate.setCustomer(customer);
        }

        return restaurantReservationRepository.save(restaurantReservationToUpdate);
    }

    public void deleteRestaurantReservation(int id) throws NotFoundException{
        RestaurantReservation restaurantReservationToDelete = getRestaurantReservation(id);

        restaurantReservationRepository.delete(restaurantReservationToDelete);
    }
}
