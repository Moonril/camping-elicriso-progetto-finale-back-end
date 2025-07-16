package it.epicode.camping_elicriso_progetto_finale_back_end.controller;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.MobileHomeDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.MobileHome;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.MobileHomeService;
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
@RequestMapping(path = "accommodations/mobilehomes")
public class MobileHomeController {
    @Autowired
    private MobileHomeService mobileHomeService;

    @GetMapping

    public Page<MobileHome> getAllMobileHomes(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        return mobileHomeService.getAllMobileHomes(page, size, sortBy);
    }

    @GetMapping("/{id}")

    public MobileHome getMobileHomeById(@PathVariable int id) throws NotFoundException {
        return mobileHomeService.getMobileHome(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public MobileHome saveMobileHome(@RequestBody @Validated MobileHomeDto mobileHomeDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return mobileHomeService.saveMobileHome(mobileHomeDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public MobileHome updateMobileHome(@PathVariable int id, @RequestBody @Validated MobileHomeDto mobileHomeDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return mobileHomeService.updateMobileHome(id, mobileHomeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteMobileHome(@PathVariable int id) throws NotFoundException {
        mobileHomeService.deleteMobileHome(id);
    }

    @GetMapping("/available")
    public List<MobileHome> getAvailableMobileHomes(
            @RequestParam int guests,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate
    ) {
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);
        return mobileHomeService.getAvailableMobileHomes(guests, checkIn, checkOut);
    }
}
