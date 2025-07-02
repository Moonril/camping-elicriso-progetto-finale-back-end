package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.LoginDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.UserRepository;
import it.epicode.camping_elicriso_progetto_finale_back_end.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String login(LoginDto loginDto) throws NotFoundException {
        User user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow(
                ()->new NotFoundException("Cannot find user with these username/password"));
        if (passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){
            return jwtTool.createToken(user);
        }else {
            throw new NotFoundException("Cannot find user with these username/password");
        }


    }
}
