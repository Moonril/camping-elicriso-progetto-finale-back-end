package it.epicode.camping_elicriso_progetto_finale_back_end.dto;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.PlotType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlotDto {
    @NotBlank(message = "The field name cannot be empty")
    private String name;
    @NotNull(message = "The field maxNumberOfPeople cannot be empty")
    private int maxNumberOfPeople;
    @NotBlank(message = "The field dimentions cannot be empty")
    private String dimentions;
    @NotNull(message = "The field price cannot be empty")
    private BigDecimal price;
    @NotBlank(message = "The field plotType cannot be empty")
    private PlotType plotType;
}
