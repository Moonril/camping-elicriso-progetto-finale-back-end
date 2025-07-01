package it.epicode.camping_elicriso_progetto_finale_back_end.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private String message;
    private LocalDateTime dataErrore;
}
