package it.epicode.camping_elicriso_progetto_finale_back_end.controller;


import it.epicode.camping_elicriso_progetto_finale_back_end.dto.LoginDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.dto.UserDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.ValidationException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.AuthService;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/register")
    public User register(@RequestBody @Validated UserDto userDto,
                         BindingResult bindingResult) throws ValidationException {

        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }
        return userService.saveUtente(userDto);
    }

    @CrossOrigin(origins = "http://localhost:63342/")
    @PostMapping("/auth/login")
    public String login(@RequestBody @Validated LoginDto loginDto,
                        BindingResult bindingResult) throws ValidationException, NotFoundException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }


        return authService.login(loginDto);
    }
}
