package com.eustache.repositories;

import com.eustache.dto.UserDTO;
import com.eustache.dto.UserResponseDTO;
import com.eustache.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {
    UserResponseDTO findByEmail(String email);
    UserResponseDTO findByUsername(String username);
    List<User> findAll();
    User save(UserDTO userDTO);
}
