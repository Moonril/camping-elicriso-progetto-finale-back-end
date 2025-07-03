package it.epicode.camping_elicriso_progetto_finale_back_end.controller;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.ReservationDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Reservation;

import it.epicode.camping_elicriso_progetto_finale_back_end.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping(path = "/camping/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<Reservation> getAllReservations(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id") String sortBy) {
        return reservationService.getAllReservations(page, size, sortBy);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF', 'GUEST')")
    public Reservation getReservationById(@PathVariable int id) throws NotFoundException {
        return reservationService.getReservation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'GUEST')")
    public Reservation saveReservation(@RequestBody @Validated ReservationDto reservationDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return reservationService.saveReservation(reservationDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Reservation updateReservation(@PathVariable int id, @RequestBody @Validated ReservationDto reservationDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return reservationService.updateReservation(id, reservationDto);
    }

    @PatchMapping("/{id}/status")
    public Reservation patchReservation(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody
    ) throws NotFoundException {
        String statusValue = requestBody.get("status");
        if (statusValue == null) {
            throw new IllegalArgumentException("Missing 'status' field");
        }

        BookingStatus newStatus = BookingStatus.valueOf(statusValue.toUpperCase());
        return reservationService.patchReservation(id, newStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteReservation(@PathVariable int id) throws NotFoundException {
        reservationService.deleteReservation(id);
    }

    // cambiare booking status in completed
    @GetMapping("/auto-complete")
    public String autoCompleteReservations() {
        reservationService.autoCompleteReservations();
        return "Completed all applicable reservations.";
    }
}
