package biojj.managerproductapi.security;

import biojj.managerproductapi.domain.enums.Profile;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSS implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;
    @Getter
    private Long id;
    private String email;
    private String password;
    @Getter
    private String name;
    @Getter
    private Boolean status;
    private Collection<? extends GrantedAuthority> authorities;


    public UserSS(Long id, String email, String password, Set<Profile> perfis, String name, Boolean status) {
        super();
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toSet());
        this.name = name;
        this.status = status;
    }

    public UserSS() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
