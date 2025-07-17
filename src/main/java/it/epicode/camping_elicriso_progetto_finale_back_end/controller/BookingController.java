package it.epicode.camping_elicriso_progetto_finale_back_end.controller;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.BookingDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.enums.BookingStatus;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Booking;

import it.epicode.camping_elicriso_progetto_finale_back_end.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/camping/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public Page<Booking> getAllBookings(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            @RequestParam(defaultValue = "id") String sortBy) {
        return bookingService.getAllBookings(page, size, sortBy);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable int id) throws NotFoundException {
        return bookingService.getBooking(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasAuthority('ADMIN')")
    public Booking saveBooking(@RequestBody @Validated BookingDto bookingDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        if (bookingDto.getCustomerId() == null && bookingDto.getCustomer() == null) {
            throw new ValidationException("Booking must include either customerId or customer data.");
        }
        return bookingService.saveBooking(bookingDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Booking updateBooking(@PathVariable int id, @RequestBody @Validated BookingDto bookingDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return bookingService.updateBooking(id, bookingDto);
    }

    @PatchMapping("/{id}/status")
    public Booking patchBooking(
            @PathVariable int id,
            @RequestBody Map<String, String> requestBody
    ) throws NotFoundException {
        String statusValue = requestBody.get("status");
        if (statusValue == null) {
            throw new IllegalArgumentException("Missing 'status' field");
        }

        BookingStatus newStatus = BookingStatus.valueOf(statusValue.toUpperCase());
        return bookingService.patchBooking(id, newStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteBooking(@PathVariable int id) throws NotFoundException {
        bookingService.deleteBooking(id);
    }

    // cambiare booking status in completed
    @GetMapping("/auto-complete")
    public String autoCompleteBookings() {
        bookingService.autoCompleteBookings();
        return "Completed all applicable bookings.";
    }
}
