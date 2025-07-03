package it.epicode.camping_elicriso_progetto_finale_back_end.service;

import com.cloudinary.Cloudinary;
import it.epicode.camping_elicriso_progetto_finale_back_end.dto.UserDto;
import it.epicode.camping_elicriso_progetto_finale_back_end.enums.UserType;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.UnauthorizedException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import it.epicode.camping_elicriso_progetto_finale_back_end.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    public User saveUser(UserDto userDto){
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUserType(UserType.STAFF);

        return userRepository.save(user);
    }

    public User getUser(int id) throws NotFoundException {
        return userRepository.findById(id).
                orElseThrow(()-> new NotFoundException("User with id: "
                        + id + " not found"));
    }

    public Page<User> getAllUsers(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userRepository.findAll(pageable);
    }

    public User updateUser(int id, UserDto userDto)
            throws NotFoundException {

        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userAuthenticated.getUserType().name().equals("ADMIN") && userAuthenticated.getId() != id) {
            throw new UnauthorizedException("You cannot modify another user.");
        }

        User userToUpdate = getUser(id);

        userToUpdate.setName(userDto.getName());
        userToUpdate.setEmail(userDto.getEmail());
        userToUpdate.setUsername(userDto.getUsername());
        userToUpdate.setSurname(userDto.getSurname());
        if (!passwordEncoder.matches(userDto.getPassword(), userToUpdate.getPassword())){
            userToUpdate.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }


        return userRepository.save(userToUpdate);
    }

    public String patchUser(int id, MultipartFile file) throws NotFoundException, IOException {
        User userToPatch = getUser(id);

        String url = (String)cloudinary.uploader().upload(file.getBytes(),
                Collections.emptyMap()).get("url");

        userToPatch.setAvatar(url);

        userRepository.save(userToPatch);

        return url;
    }


    public void deleteUser(int id) throws NotFoundException {
        User userAuthenticated = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!userAuthenticated.getUserType().name().equals("ADMIN") && userAuthenticated.getId() != id) {
            throw new UnauthorizedException("You cannot delete another user.");
        }

        User userToDelete = getUser(id);

        userRepository.delete(userToDelete);
    }

}
