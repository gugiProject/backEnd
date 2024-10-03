package com.boot.gugi.service;

import com.boot.gugi.base.Enum.*;
import com.boot.gugi.base.dto.UserDTO;
import com.boot.gugi.base.util.TranslationUtil;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserDTO userDTO) {

        Sex gender = TranslationUtil.fromKorean(userDTO.getGender(), Sex.class);
        if (gender == null) {
            throw new IllegalArgumentException("성별이 유효하지 않습니다.");
        }

        Team team = TranslationUtil.fromKorean(userDTO.getTeam(), Team.class);
        if (team == null) {
            throw new IllegalArgumentException("팀 이름이 유효하지 않습니다.");
        }

        User user = User.builder()
                .name(userDTO.getName())
                .nickName(userDTO.getNickName())
                .email(userDTO.getEmail())
                .gender(TranslationUtil.toEnglish(gender))
                .age(userDTO.getAge())
                .team(TranslationUtil.toEnglish(team))
                .profileImg(userDTO.getProfileImg())
                .introduction(userDTO.getIntroduction())
                .build();

        return userRepository.save(user);
    }
}
