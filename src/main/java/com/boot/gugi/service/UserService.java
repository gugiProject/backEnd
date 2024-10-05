package com.boot.gugi.service;

import com.amazonaws.AmazonServiceException;
import com.boot.gugi.base.Enum.*;
import com.boot.gugi.base.dto.UserDTO;
import com.boot.gugi.base.util.TranslationUtil;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private S3Service s3Service;

    public User createUser(UserDTO userDTO, MultipartFile profileImg) {

        Sex gender = TranslationUtil.fromKorean(userDTO.getGender(), Sex.class);
        if (gender == null) {
            throw new IllegalArgumentException("성별이 유효하지 않습니다.");
        }
        Team team = TranslationUtil.fromKorean(userDTO.getTeam(), Team.class);
        if (team == null) {
            throw new IllegalArgumentException("팀 이름이 유효하지 않습니다.");
        }

        String profileImgUrl = null;
        if (profileImg != null && !profileImg.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + profileImg.getOriginalFilename();
                profileImgUrl = s3Service.uploadFile(profileImg.getInputStream(), fileName);
            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 업로드 실패: " + e.getMessage(), e);
            } catch (AmazonServiceException e) {
                throw new RuntimeException("S3 서비스 오류: " + e.getMessage(), e);
            }
        }

        User user = User.builder()
                .name(userDTO.getName())
                .nickName(userDTO.getNickName())
                .email(userDTO.getEmail())
                .gender(TranslationUtil.toEnglish(gender))
                .age(userDTO.getAge())
                .team(TranslationUtil.toEnglish(team))
                .profileImg(profileImgUrl)
                .introduction(userDTO.getIntroduction())
                .build();

        return userRepository.save(user);
    }
}
