package it.epicode.camping_elicriso_progetto_finale_back_end.controller;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.RestaurantReservationDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.RestaurantReservation;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.RestaurantReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/restaurant/reservations")
public class RestaurantReservationController {
    @Autowired
    private RestaurantReservationService restaurantReservationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<RestaurantReservation> getAllRestaurantReservations(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size,
                                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return restaurantReservationService.getAllRestaurantReservations(page, size, sortBy);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public RestaurantReservation getRestaurantReservationById(@PathVariable int id) throws NotFoundException {
        return restaurantReservationService.getRestaurantReservation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:5173/")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public RestaurantReservation saveRestaurantReservation(@RequestBody @Validated RestaurantReservationDto restaurantReservationDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return restaurantReservationService.saveRestaurantReservation(restaurantReservationDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public RestaurantReservation updateRestaurantReservation(@PathVariable int id, @RequestBody @Validated RestaurantReservationDto restaurantReservationDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return restaurantReservationService.updateRestaurantReservation(id, restaurantReservationDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteRestaurantReservation(@PathVariable int id) throws NotFoundException {
        restaurantReservationService.deleteRestaurantReservation(id);
    }
}
