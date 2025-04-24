package com.eustache.mapper;

import com.eustache.dto.UserDTO;
import com.eustache.dto.UserResponseDTO;
import com.eustache.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    //Convert a UserDTO object to a User Entity
    public User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new NullPointerException("userDTO is null");
        }
        var user = new User();
        user.setUsername(userDTO.username());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        return user;
    }

    //Convert User entity to UserResponseDTO
    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(user.getUsername(), user.getEmail());
    }
}
