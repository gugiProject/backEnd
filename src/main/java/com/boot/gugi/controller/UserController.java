package com.boot.gugi.controller;

import com.boot.gugi.base.dto.UserDTO;
import com.boot.gugi.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createUser(
            @RequestPart("userDTO") @Valid String userDTOJson,
            @RequestPart(value = "profileImg", required = false) MultipartFile profileImg) {

        ObjectMapper objectMapper = new ObjectMapper();
        UserDTO userDTO;
        try {
            userDTO = objectMapper.readValue(userDTOJson, UserDTO.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

        userService.createUser(userDTO,profileImg);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}