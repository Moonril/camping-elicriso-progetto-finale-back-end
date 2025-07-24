package it.epicode.camping_elicriso_progetto_finale_back_end.controller;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.GlampingDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Glamping;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.GlampingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "accommodations/glampings")
public class GlampingController {
    @Autowired
    private GlampingService glampingService;

    @GetMapping
    //@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Page<Glamping> getAllGlampings(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int size,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        return glampingService.getAllGlampings(page, size, sortBy);
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyAuthority('ADMIN', 'STAFF')")
    public Glamping getGlampingById(@PathVariable int id) throws NotFoundException {
        return glampingService.getGlamping(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Glamping saveGlamping(@RequestBody @Validated GlampingDto glampingDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return glampingService.saveGlamping(glampingDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Glamping updateGlamping(@PathVariable int id, @RequestBody @Validated GlampingDto glampingDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return glampingService.updateGlamping(id, glampingDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteGlamping(@PathVariable int id) throws NotFoundException {
        glampingService.deleteGlamping(id);
    }

    @GetMapping("/available")
    public List<Glamping> getAvailableGlampings(
            @RequestParam int guests,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate
    ) {
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);
        return glampingService.getAvailableGlampings(guests, checkIn, checkOut);
    }
}
