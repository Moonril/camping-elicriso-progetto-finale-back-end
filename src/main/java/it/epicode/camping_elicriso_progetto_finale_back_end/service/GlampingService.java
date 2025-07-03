package it.epicode.camping_elicriso_progetto_finale_back_end.service;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.GlampingDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Glamping;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.GlampingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class GlampingService {

    @Autowired
    private GlampingRepository glampingRepository;

    public Glamping saveGlamping(GlampingDto glampingDto) throws NotFoundException {
        Glamping glamping = new Glamping();

        glamping.setName(glampingDto.getName());
        glamping.setMaxNumberOfPeople(glampingDto.getMaxNumberOfPeople());
        glamping.setDimentions(glampingDto.getDimentions());
        glamping.setPrice(glampingDto.getPrice());
        glamping.setNumberOfBeds(glampingDto.getNumberOfBeds());
        glamping.setAirConditioning(glampingDto.isAirConditioning());

        return glampingRepository.save(glamping);
    }

    public Page<Glamping> getAllGlampings(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return glampingRepository.findAll(pageable);
    }

    public Glamping getGlamping(int id) throws NotFoundException{
        return glampingRepository.findById(id).orElseThrow(() -> new NotFoundException("Glamping with id: " + id + " not found"));
    }


    public Glamping updateGlamping(int id, GlampingDto glampingDto) throws NotFoundException{
        Glamping glampingToUpdate = getGlamping(id);

        glampingToUpdate.setName(glampingDto.getName());
        glampingToUpdate.setMaxNumberOfPeople(glampingDto.getMaxNumberOfPeople());
        glampingToUpdate.setDimentions(glampingDto.getDimentions());
        glampingToUpdate.setPrice(glampingDto.getPrice());
        glampingToUpdate.setNumberOfBeds(glampingDto.getNumberOfBeds());
        glampingToUpdate.setAirConditioning(glampingDto.isAirConditioning());

        return glampingRepository.save(glampingToUpdate);
    }

    public void deleteGlamping(int id) throws NotFoundException{
        Glamping glampingToDelete = getGlamping(id);

        glampingRepository.delete(glampingToDelete);
    }
}
