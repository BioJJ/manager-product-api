package biojj.managerproductapi.service;

import biojj.managerproductapi.domain.dto.UserDTO;
import biojj.managerproductapi.domain.mapper.UserMapper;
import biojj.managerproductapi.domain.model.User;
import biojj.managerproductapi.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder encoder;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    public UserDTO save(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(dto.getPassword()));

        User savedUser = userRepository.save(user);
        log.info("User salvo com ID: {}", savedUser.getId());

        return userMapper.toDTO(savedUser);
    }

    public UserDTO getByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::toDTO).orElse(null);
    }

    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElse(null);
    }

    public UserDTO updateById(Long id, UserDTO dto) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    userMapper.updateFromDto(dto, existingUser);
                    User updatedUser = userRepository.save(existingUser);
                    return userMapper.toDTO(updatedUser);
                })
                .orElse(null);
    }

    public Page<UserDTO> getAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size))
                .map(userMapper::toDTO);
    }
}
