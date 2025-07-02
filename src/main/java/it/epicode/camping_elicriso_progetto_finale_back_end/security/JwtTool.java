package it.epicode.camping_elicriso_progetto_finale_back_end.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {

    @Value("${jwt.duration}")
    private Long duration;
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private UserService userService;

    public String createToken(User user){

        return Jwts.builder().issuedAt(new Date()).
                expiration(new Date(System.currentTimeMillis()+duration))
                .subject(user.getId()+"").signWith(Keys.
                        hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public void validateToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build().parse(token);
    }

    public User getUserFromToken(String token) throws NotFoundException {
        int id = Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build().parseSignedClaims(token).getPayload().getSubject());

        return userService.getUser(id);
    }
}
