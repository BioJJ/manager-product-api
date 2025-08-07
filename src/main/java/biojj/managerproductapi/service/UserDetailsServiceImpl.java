package biojj.managerproductapi.service;

import biojj.managerproductapi.domain.enums.Profile;
import biojj.managerproductapi.domain.model.User;
import biojj.managerproductapi.repository.UserRepository;
import biojj.managerproductapi.security.UserSS;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User userEntity = user.get();
            Set<Profile> profiles = convertCodesToProfiles(userEntity.getProfiles());
            return new UserSS(
                    userEntity.getId(),
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    profiles,
                    userEntity.getName(),
                    userEntity.getStatus()
            );
        }
        throw new UsernameNotFoundException(email);
    }

    private Set<Profile> convertCodesToProfiles(Set<Integer> codes) {
        return codes.stream()
                .map(Profile::fromCode)
                .collect(Collectors.toSet());
    }
}