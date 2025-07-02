package it.epicode.camping_elicriso_progetto_finale_back_end.controller;

import it.epicode.camping_elicriso_progetto_finale_back_end.dto.UserDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import it.epicode.camping_elicriso_progetto_finale_back_end.service.UserService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    public User saveUtente(@RequestBody @Validated UserDto userDto, BindingResult bindingResult)
            throws ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }

        return userService.saveUser(userDto);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUser(@PathVariable int id) throws NotFoundException {
        return userService.getUser(id);
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(defaultValue = "id") String sortBy){
        return userService.getAllUsers(page, size, sortBy);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public User updateUtente(@PathVariable int id, @RequestBody
                               @Validated UserDto userDto,
                             BindingResult bindingResult) throws NotFoundException, ValidationException {

        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().
                    stream().map(objectError -> objectError.getDefaultMessage())
                    .reduce("",(e,s)->e+s));
        }
        return userService.updateUser(id, userDto);
    }

    @PatchMapping("/{id}")
    public String patchUser(@PathVariable int id, @RequestBody MultipartFile file)
            throws NotFoundException, IOException {
        return userService.patchUser(id, file);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public void deleteUser(@PathVariable int id) throws NotFoundException {
        userService.deleteUser(id);
    }
}
