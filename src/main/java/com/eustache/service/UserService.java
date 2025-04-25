package com.eustache.service;

import com.eustache.dto.UserDTO;
import com.eustache.dto.UserResponseDTO;
import com.eustache.mapper.UserMapper;
import com.eustache.models.User;
import com.eustache.repositories.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JWTService jWTService;


    public UserService(UserRepository userRepository, UserMapper userMapper, @Lazy AuthenticationManager authenticationManager, JWTService jWTService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jWTService = jWTService;
    }

    //Register
    @Transactional
    public UserResponseDTO registerUser(UserDTO userDTO) {
        var user = userMapper.toUser(userDTO);
        System.out.println("BEFORE SAVE: " + user);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(user.getCreatedAt());
        user.setUpdatedAt(user.getUpdatedAt());
        var savedUser = userRepository.save(user);
        System.out.println("AFTER SAVE: " + savedUser);
        return userMapper.toUserResponseDTO(savedUser);
    }


    //Get by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //Get by email
    public UserResponseDTO findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //Get all the users
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public String verify(UserDTO userDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDTO.username(), userDTO.password())
        );

        if (authentication.isAuthenticated()) {
            return jWTService.generateToken(userDTO.username());
        }
        return "Failed";
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new DetailsUser(user);
    }
}
