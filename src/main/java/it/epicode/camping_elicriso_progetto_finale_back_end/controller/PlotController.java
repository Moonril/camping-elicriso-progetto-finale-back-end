package it.epicode.camping_elicriso_progetto_finale_back_end.controller;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.PlotDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Plot;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.PlotService;
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
@RequestMapping(path = "accommodations/plots")
public class PlotController {
    @Autowired
    private PlotService plotService;

    @GetMapping

    public Page<Plot> getAllPlots(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "20") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return plotService.getAllPlots(page, size, sortBy);
    }

    @GetMapping("/{id}")

    public Plot getPlotById(@PathVariable int id) throws NotFoundException {
        return plotService.getPlot(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Plot savePlot(@RequestBody @Validated PlotDto plotDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return plotService.savePlot(plotDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Plot updatePlot(@PathVariable int id, @RequestBody @Validated PlotDto plotDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage()).reduce("", (e, s) -> e + s));
        }
        return plotService.updatePlot(id, plotDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deletePlot(@PathVariable int id) throws NotFoundException {
        plotService.deletePlot(id);
    }

    @GetMapping("/available")
    public List<Plot> getAvailablePlots(
            @RequestParam int guests,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate
    ) {
        LocalDate checkIn = LocalDate.parse(checkInDate);
        LocalDate checkOut = LocalDate.parse(checkOutDate);
        return plotService.getAvailablePlots(guests, checkIn, checkOut);
    }
    
    
}
