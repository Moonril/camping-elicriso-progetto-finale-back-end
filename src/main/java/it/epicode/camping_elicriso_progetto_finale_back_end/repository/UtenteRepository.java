package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer>,
        PagingAndSortingRepository<Utente, Integer> {
    public Optional<Utente> findByUsername(String username);
}

