package com.boot.gugi.service;

import com.boot.gugi.base.dto.UserDTO;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    /*
    public User findById(UUID userId) {

        return userRepository.findById(userId).orElse(null);
    }
     */

    public User createUser(UserDTO userDTO) {
        User user = User.builder()
                .name(userDTO.getName())
                .nickName(userDTO.getNickName())
                .email(userDTO.getEmail())
                .gender(userDTO.getGender())
                .age(userDTO.getAge())
                .team(userDTO.getTeam())
                .profileImg(userDTO.getProfileImg())
                .introduction(userDTO.getIntroduction())
                .build();

        return userRepository.save(user);
    }
}
