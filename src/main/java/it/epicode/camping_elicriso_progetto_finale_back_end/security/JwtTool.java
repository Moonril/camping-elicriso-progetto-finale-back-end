package it.epicode.camping_elicriso_progetto_finale_back_end.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.Utente;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {

    @Value("${jwt.duration}")
    private Long durata;
    @Value("${jwt.secret}")
    private String chiaveSegreta;

    @Autowired
    private UtenteService utenteService;

    public String createToken(Utente utente){

        return Jwts.builder().issuedAt(new Date()).
                expiration(new Date(System.currentTimeMillis()+durata))
                .subject(utente.getId()+"").signWith(Keys.
                        hmacShaKeyFor(chiaveSegreta.getBytes()))
                .compact();
    }

    public void validateToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build().parse(token);
    }

    public Utente getUtenteFromToken(String token) throws NotFoundException {
        int id = Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes()))
                .build().parseSignedClaims(token).getPayload().getSubject());

        return utenteService.getUtente(id);
    }
}
