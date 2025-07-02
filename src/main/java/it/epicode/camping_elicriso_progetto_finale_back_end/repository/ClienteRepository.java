package it.epicode.camping_elicriso_progetto_finale_back_end.repository;

import it.epicode.camping_elicriso_progetto_finale_back_end.models.Cliente;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>,
        PagingAndSortingRepository<Cliente, Integer> {
}
