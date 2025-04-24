package com.eustache.controller.auth;

import com.eustache.dto.UserDTO;
import com.eustache.dto.UserResponseDTO;
import com.eustache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody UserDTO user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password) {
        return "success logging in";
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
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp){
        //Create a map to store field names and their corresponding error message
        var errors = new HashMap<String,String>();

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
