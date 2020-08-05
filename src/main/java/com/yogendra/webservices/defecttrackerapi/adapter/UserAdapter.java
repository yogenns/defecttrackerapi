package com.yogendra.webservices.defecttrackerapi.adapter;

import com.yogendra.webservices.defecttrackerapi.data.UserData;
import com.yogendra.webservices.defecttrackerapi.data.UserIdAndName;
import com.yogendra.webservices.defecttrackerapi.jpa.UserJPARepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserAdapter {
    @Autowired
    private UserJPARepository userJPARepository;

    //Get all users. This should not be part of production
    @GetMapping(path = "/users")
    public List<UserIdAndName> getAllProjects() {
        System.out.println("getAllUsers");
        return userJPARepository.findAllBy();
    }

    @PostMapping(path = "/user")
    public ResponseEntity<Object> isAuthenticatedUser(@RequestBody UserData userData) {
        StringBuilder errorMessage = new StringBuilder();
        if (userData == null) {
            System.out.println("UserData is required. ");
            errorMessage.append("UserData is required. ");
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (userData.getUsername() == null || userData.getUsername().isEmpty()) {
            System.out.println("UserName is required. ");
            errorMessage.append("UserName is required. ");
        }
        if (userData.getPassword() == null || userData.getPassword().isEmpty()) {
            System.out.println("Password is required. ");
            errorMessage.append("Password is required. ");
        }

        if (errorMessage.length() == 0) {
            UserData dbData = userJPARepository.findByUsername(userData.getUsername());
            Map<String, String> responseMap = new HashMap<>();
            if (dbData != null && dbData.getUsername().equals(userData.getUsername())
                    && dbData.getPassword().equals(userData.getPassword())) {
                responseMap.put("status", "Success");
                responseMap.put("message", "User " + userData.getUsername() + " authenticated Successfully");
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("status", "Failure");
                responseMap.put("error", "User authentication failed");
                return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/register")
    public ResponseEntity<Object> addNewUser(@RequestBody UserData userData) {
        StringBuilder errorMessage = new StringBuilder();
        if (userData == null) {
            System.out.println("UserData is required. ");
            errorMessage.append("UserData is required. ");
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userData.getUsername() == null || userData.getUsername().isEmpty()) {
            System.out.println("UserName is required. ");
            errorMessage.append("UserName is required. ");
        } else {
            UserData dbData = userJPARepository.findByUsername(userData.getUsername());
            if (dbData != null) {
                System.out.println("UserName already exists. ");
                errorMessage.append("UserName already exists. ");
            }
        }
        if (userData.getPassword() == null || userData.getPassword().isEmpty()) {
            System.out.println("Password is required. ");
            errorMessage.append("Password is required. ");
        }
        if (errorMessage.length() == 0) {
            UserData createdUserData = userJPARepository.save(userData);
            System.out.println("Add User " + createdUserData);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("status", "Success");
            responseMap.put("message", "User Added Successfully with ID " + createdUserData.getId());
            return new ResponseEntity<>(responseMap, HttpStatus.OK);
        } else {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("status", "Failure");
            errorMap.put("error", errorMessage.toString());
            return new ResponseEntity<>(errorMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
