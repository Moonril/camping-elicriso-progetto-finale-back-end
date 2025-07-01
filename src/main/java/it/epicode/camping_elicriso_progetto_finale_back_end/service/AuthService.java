package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.LoginDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Utente;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.UtenteRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String login(LoginDto loginDto) throws NotFoundException {
        Utente utente = utenteRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                ()->new NotFoundException("Utente con username/password non trovato"));
        if (passwordEncoder.matches(loginDto.getPassword(), utente.getPassword())){
            return jwtTool.createToken(utente);
        }else {
            throw new NotFoundException("Utente con username/password non trovato");
        }


    }
}
