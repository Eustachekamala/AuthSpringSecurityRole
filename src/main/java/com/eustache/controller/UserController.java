package com.eustache.controller;

import com.eustache.dto.UserDTO;
import com.eustache.dto.UserResponseDTO;
import com.eustache.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Application";
    }

    @GetMapping("/users")
    public List<UserResponseDTO> users() {
        return userService.findAll();
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserDTO userDTO) {
        System.out.println("REGISTER: " + userDTO);
        return userService.registerUser(userDTO);
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO userDTO) {
        return userService.verify(userDTO);
    }

    @PostMapping("/logout")
    public String logout() {
        return "success";
    }

    /*
     * Handles exceptions of type MethodArgumentNotValidException that occur when
     * validation on a method argument annotated with @Valid fails.
     *
     * @param exp the MethodArgumentNotValidException containing details about the validation errors
     * @return a ResponseEntity containing a map of field names and their corresponding error messages,
     *         along with a BAD_REQUEST (400) HTTP status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
        //Create a map to store field names and their corresponding error message
        var errors = new HashMap<String, String>();

        //Iterate through all validation errors and populate the map
        exp.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField(); // Extract the field name
            var errorMessage = error.getDefaultMessage(); // Extract the error message
            errors.put(fieldName, errorMessage);
        });

        /// Return the map of errors wrapped in a ResponseEntity with a BAD_REQUEST status
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
