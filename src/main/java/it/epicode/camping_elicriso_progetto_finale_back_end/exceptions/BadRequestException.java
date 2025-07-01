package it.epicode.camping_elicriso_progetto_finale_back_end.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
