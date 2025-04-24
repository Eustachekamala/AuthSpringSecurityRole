package com.eustache.service;

import com.eustache.dto.UserDTO;
import com.eustache.dto.UserResponseDTO;
import com.eustache.mapper.UserMapper;
import com.eustache.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    //Register
    public UserResponseDTO registerUser(UserDTO userDTO) {
        var user = userMapper.toUser(userDTO);
        user.setPassword(encoder.encode(user.getPassword()));
        var savedUser = userRepository.save(userDTO);
        return userMapper.toUserResponseDTO(savedUser);
    }

    //Get by username
    public UserResponseDTO findByUsername(String username) {
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
}
