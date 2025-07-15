package it.epicode.camping_elicriso_progetto_finale_back_end.service;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.MobileHomeDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.MobileHome;

import it.epicode.camping_elicriso_progetto_finale_back_end.repository.MobileHomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MobileHomeService {
    @Autowired
    private MobileHomeRepository mobileHomeRepository;

    public MobileHome saveMobileHome(MobileHomeDto mobileHomeDto) throws NotFoundException {
        MobileHome mobileHome = new MobileHome();

        mobileHome.setName(mobileHomeDto.getName());
        mobileHome.setMaxNumberOfPeople(mobileHomeDto.getMaxNumberOfPeople());
        mobileHome.setDimentions(mobileHomeDto.getDimentions());
        mobileHome.setPrice(mobileHomeDto.getPrice());
        mobileHome.setNumberOfBeds(mobileHomeDto.getNumberOfBeds());
        mobileHome.setNumberOfBedrooms(mobileHomeDto.getNumberOfBedrooms());
        mobileHome.setAirConditioning(mobileHomeDto.isAirConditioning());
        mobileHome.setMobileType(mobileHomeDto.getMobileType());

        return mobileHomeRepository.save(mobileHome);
    }

    public Page<MobileHome> getAllMobileHomes(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return mobileHomeRepository.findAll(pageable);
    }

    public MobileHome getMobileHome(int id) throws NotFoundException{
        return mobileHomeRepository.findById(id).orElseThrow(() -> new NotFoundException("MobileHome with id: " + id + " not found"));
    }


    public MobileHome updateMobileHome(int id, MobileHomeDto mobileHomeDto) throws NotFoundException{
        MobileHome mobileHomeToUpdate = getMobileHome(id);

        mobileHomeToUpdate.setName(mobileHomeDto.getName());
        mobileHomeToUpdate.setMaxNumberOfPeople(mobileHomeDto.getMaxNumberOfPeople());
        mobileHomeToUpdate.setDimentions(mobileHomeDto.getDimentions());
        mobileHomeToUpdate.setPrice(mobileHomeDto.getPrice());
        mobileHomeToUpdate.setNumberOfBeds(mobileHomeDto.getNumberOfBeds());
        mobileHomeToUpdate.setNumberOfBedrooms(mobileHomeDto.getNumberOfBedrooms());
        mobileHomeToUpdate.setAirConditioning(mobileHomeDto.isAirConditioning());
        mobileHomeToUpdate.setMobileType(mobileHomeDto.getMobileType());

        return mobileHomeRepository.save(mobileHomeToUpdate);
    }

    public void deleteMobileHome(int id) throws NotFoundException{
        MobileHome mobileHomeToDelete = getMobileHome(id);

        mobileHomeRepository.delete(mobileHomeToDelete);
    }
}
