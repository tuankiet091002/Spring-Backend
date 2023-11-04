package com.java.java_proj.util;

import com.java.java_proj.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
public class CustomUserDetail implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String[] authorityList = {
                user.getRole().getRole(),
                "SYLLABUS_" + user.getRole().getSyllabus().toString(),
                "TRAININGPROGRAM_" + user.getRole().getTrainingProgram().toString(),
                "CLASSMANAGEMENT_" + user.getRole().getClassManagement().toString(),
                "LEARNINGMATERIAL_" + user.getRole().getLearningMaterial().toString(),
                "USERMANAGEMENT_" + user.getRole().getUserManagement().toString()
        };
        return List.of(
                new SimpleGrantedAuthority(authorityList[0]),
                new SimpleGrantedAuthority(authorityList[1]),
                new SimpleGrantedAuthority(authorityList[2]),
                new SimpleGrantedAuthority(authorityList[3]),
                new SimpleGrantedAuthority(authorityList[4]),
                new SimpleGrantedAuthority(authorityList[5])
        );

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
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


