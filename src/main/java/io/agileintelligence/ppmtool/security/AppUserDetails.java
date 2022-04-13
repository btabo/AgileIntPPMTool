package io.agileintelligence.ppmtool.security;

import io.agileintelligence.ppmtool.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Getter @NoArgsConstructor
public class AppUserDetails implements UserDetails {

    private Long id;

    private String username;

    private String password;

    private String fullName;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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

    public static AppUserDetails from(User user) {
        AppUserDetails appUserDetails = new AppUserDetails();
        appUserDetails.id = user.getId();
        appUserDetails.username = user.getUsername();
        appUserDetails.password = user.getPassword();
        appUserDetails.fullName = user.getFullName();
        return appUserDetails;
    }
}
