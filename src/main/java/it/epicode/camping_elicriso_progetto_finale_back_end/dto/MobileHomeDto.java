package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MobileHomeDto {
    @NotBlank(message = "The field name cannot be empty")
    private String name;
    @NotNull(message = "The field maxNumberOfPeople cannot be empty")
    private int maxNumberOfPeople;
    @NotBlank(message = "The field dimentions cannot be empty")
    private String dimentions;
    @NotNull(message = "The field price cannot be empty")
    private BigDecimal price;
    @NotNull(message = "The field numberOfBeds cannot be empty")
    private int numberOfBeds;
    @NotNull(message = "The field numberOfBedrooms cannot be empty")
    private int numberOfBedrooms;
    @NotNull(message = "The field airContitioning cannot be empty")
    private boolean airConditioning;
}
