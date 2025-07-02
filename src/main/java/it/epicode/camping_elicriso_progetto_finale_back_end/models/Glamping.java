package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "glamping")
public class Glamping extends Accomodation{
    private int numberOfBeds;
    private boolean airConditioning;
}
