package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.MobileType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "mobile_homes")
public class MobileHome extends Accommodation {
    private int numberOfBeds;
    private int numberOfBedrooms;
    private boolean airConditioning;
    private MobileType mobileType;
}
