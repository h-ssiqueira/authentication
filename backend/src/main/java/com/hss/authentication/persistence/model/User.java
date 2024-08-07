package com.hss.authentication.persistence.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    @Formula("lower(username)")
    private String username;

    @Column(name = "email")
    @Formula("lower(email)")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updatePassword(String password, String salt) {
        this.setPassword(password);
        this.setSalt(salt);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role.equals(Role.ADMIN)) {
            return List.of(new SimpleGrantedAuthority("ADMIN_ROLE"), new SimpleGrantedAuthority("USER_ROLE"));
        }
        return List.of(new SimpleGrantedAuthority("USER_ROLE"));
    }

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