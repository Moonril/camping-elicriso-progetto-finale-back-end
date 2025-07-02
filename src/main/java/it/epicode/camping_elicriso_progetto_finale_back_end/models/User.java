package it.epicode.camping_elicriso_progetto_finale_back_end.models;

import it.epicode.camping_elicriso_progetto_finale_back_end.enums.UserType;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String surname;

    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;

    private String avatar;
    @Enumerated(EnumType.STRING)
    private UserType userType;

    //che è sta roba?
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userType.name()));
    }

    //roba per testare in contesti non reali
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
