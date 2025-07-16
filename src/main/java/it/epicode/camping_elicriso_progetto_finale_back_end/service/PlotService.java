package it.epicode.camping_elicriso_progetto_finale_back_end.service;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.PlotDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Plot;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.PlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PlotService {
    @Autowired
    private PlotRepository plotRepository;

    public Plot savePlot(PlotDto plotDto) throws NotFoundException {
        Plot plot = new Plot();

        plot.setName(plotDto.getName());
        plot.setMaxNumberOfPeople(plotDto.getMaxNumberOfPeople());
        plot.setDimentions(plotDto.getDimentions());
        plot.setPrice(plotDto.getPrice());
        plot.setPlotType(plotDto.getPlotType());

        return plotRepository.save(plot);
    }

    public Page<Plot> getAllPlots(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return plotRepository.findAll(pageable);
    }

    public Plot getPlot(int id) throws NotFoundException{
        return plotRepository.findById(id).orElseThrow(() -> new NotFoundException("Plot with id: " + id + " not found"));
    }


    public Plot updatePlot(int id, PlotDto plotDto) throws NotFoundException{
        Plot plotToUpdate = getPlot(id);

        plotToUpdate.setName(plotDto.getName());
        plotToUpdate.setMaxNumberOfPeople(plotDto.getMaxNumberOfPeople());
        plotToUpdate.setDimentions(plotDto.getDimentions());
        plotToUpdate.setPrice(plotDto.getPrice());
        plotToUpdate.setPlotType(plotDto.getPlotType());

        return plotRepository.save(plotToUpdate);
    }

    public void deletePlot(int id) throws NotFoundException{
        Plot plotToDelete = getPlot(id);

        plotRepository.delete(plotToDelete);
    }

    public List<Plot> getAvailablePlots(int guests, LocalDate checkInDate, LocalDate checkOutDate) {
        return plotRepository.findAvailablePlots(guests, checkInDate, checkOutDate);
    }

}
