package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Prenotazione;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer>,
        PagingAndSortingRepository<Prenotazione, Integer> {
}
