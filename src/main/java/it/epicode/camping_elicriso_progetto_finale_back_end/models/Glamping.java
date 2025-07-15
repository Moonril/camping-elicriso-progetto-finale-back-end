package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.GlampingType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "glampings")
public class Glamping extends Accommodation {
    private int numberOfBeds;
    private boolean airConditioning;
    private GlampingType glampingType;
}
