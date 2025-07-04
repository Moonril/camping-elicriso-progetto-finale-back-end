package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Glamping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GlampingRepository extends JpaRepository<Glamping, Integer>,
        PagingAndSortingRepository<Glamping, Integer> {
}
