package com.boot.gugi.service;

import com.boot.gugi.base.Enum.*;
import com.boot.gugi.base.dto.MateRequestDTO;
import com.boot.gugi.base.dto.MateResponseDTO;
import com.boot.gugi.base.util.TranslationUtil;
import com.boot.gugi.exception.MatePostException;
import com.boot.gugi.model.MatePostApplicant;
import com.boot.gugi.model.MatePostStatus;
import com.boot.gugi.model.MatePost;
import com.boot.gugi.base.dto.MateDTO;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MatePostService {

    private final MatePostRepository matePostRepository;
    private final MatePostStatusRepository matePostStatusRepository;
    private final UserRepository userRepository;
    private final MatePostApplicantRepository applicantRepository;

    UserService userService;

    @Autowired
    public MatePostService(@Qualifier("matePostRepository") MatePostRepository mateRepository,
                           MatePostStatusRepository matePostStatusRepository,
                           UserRepository userRepository,
                           MatePostApplicantRepository applicantRepository) {
        this.matePostRepository = mateRepository;
        this.matePostStatusRepository = matePostStatusRepository;
        this.userRepository = userRepository;
        this.applicantRepository = applicantRepository;
    }

    public User getUserById(UUID ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    public MatePost findPostById(UUID postId) {
        Optional<MatePost> optionalPost = matePostRepository.findById(postId);
        return optionalPost.orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public MatePost createMatePost(MateDTO dto, User owner) {

        MatePost matePost = fromDTO(dto, owner);
        matePost.setParticipants(1);

        MatePost savedPost = matePostRepository.save(matePost);

        MatePostStatus postStatus = new MatePostStatus();
        postStatus.setUser(owner);
        postStatus.setMatePost(savedPost);
        postStatus.setPostStatus(MateStatus.CREATOR);

        matePostStatusRepository.save(postStatus);

        return savedPost;
    }

    public MatePost updateMatePost(UUID postId, MateDTO dto) {
        MatePost existingPost = findPostById(postId);
        updatePostFromDTO(existingPost, dto);
        existingPost.setUpdatedTimeAt(LocalDateTime.now());

        return matePostRepository.save(existingPost);
    }

    public void applyForMatePost(MateRequestDTO mateRequestDTO) {

        MatePost matePost = matePostRepository.findById(mateRequestDTO.getPostId())
                .orElseThrow(() -> new MatePostException("게시물이 존재하지 않습니다."));

        User applicant = userRepository.findById(mateRequestDTO.getUserId())
                .orElseThrow(() -> new MatePostException("신청자가 존재하지 않습니다."));

        if (applicant.getId().equals(matePost.getOwner().getId())) {
            throw new MatePostException("작성자는 자신의 게시물에 신청할 수 없습니다.");
        }

        MatePostApplicant applicantEntry = new MatePostApplicant();
        applicantEntry.setMatePost(matePost);
        applicantEntry.setApplicant(applicant);
        applicantEntry.setOwner(matePost.getOwner());
        applicantEntry.setStatus(MateStatus.PENDING);
        applicantEntry.setApplicationDate(LocalDateTime.now());
        applicantRepository.save(applicantEntry);

        MatePostStatus postStatus = new MatePostStatus();
        postStatus.setMatePost(matePost);
        postStatus.setUser(applicant);
        postStatus.setPostStatus(MateStatus.PENDING);
        matePostStatusRepository.save(postStatus);
    }

    public void approveApplication(MateResponseDTO response) {
        MatePostApplicant applicant = applicantRepository.findByApplicantIdAndMatePostId(response.getUserId(), response.getPostId())
                .orElseThrow(() -> new MatePostException("신청자가 존재하지 않거나, 게시물이 일치하지 않습니다."));

        MatePost matePost = applicant.getMatePost();
        if (matePost.getParticipants() >= matePost.getTotalMembers()) {
            throw new MatePostException("모집이 이미 완료되었습니다. 더 이상 신청을 승인할 수 없습니다.");
        }

        applicant.setStatus(MateStatus.ACCEPTED);
        applicantRepository.save(applicant);
        matePost.setParticipants(matePost.getParticipants() + 1);
        matePostRepository.save(matePost);

        MatePostStatus existingStatus = matePostStatusRepository.findByUserAndMatePost(applicant.getApplicant(), matePost)
                .orElseThrow(() -> new MatePostException("게시물 상태가 존재하지 않습니다."));

        existingStatus.setPostStatus(MateStatus.ACCEPTED);
        matePostStatusRepository.save(existingStatus);
    }

    public void rejectApplication(MateResponseDTO response) {
        MatePostApplicant applicant = applicantRepository.findByApplicantIdAndMatePostId(response.getUserId(), response.getPostId())
                .orElseThrow(() -> new MatePostException("신청자가 존재하지 않거나, 게시물이 일치하지 않습니다."));

        applicant.setStatus(MateStatus.REJECTED);
        applicantRepository.save(applicant);

        MatePostStatus existingStatus = matePostStatusRepository.findByUserAndMatePost(applicant.getApplicant(), applicant.getMatePost())
                .orElseThrow(() -> new MatePostException("게시물 상태가 존재하지 않습니다."));

        existingStatus.setPostStatus(MateStatus.REJECTED);
        matePostStatusRepository.save(existingStatus);
    }

    private MatePost fromDTO(MateDTO dto, User owner) {

        GenderPreference genderPreference = TranslationUtil.fromKorean(dto.getGender(), GenderPreference.class);
        if (genderPreference == null) { genderPreference = GenderPreference.ANY; }

        AgeGroup age = TranslationUtil.fromKorean(dto.getAgeGroup(), AgeGroup.class);
        if (age == null) { age = AgeGroup.ANY; }

        Team team = TranslationUtil.fromKorean(dto.getTeam(), Team.class);
        if (team == null) { team = Team.ANY; }

        Stadium stadium = TranslationUtil.fromKorean(dto.getStadium(), Stadium.class);
        if (stadium == null) { stadium = Stadium.ANY; }

        return MatePost.builder()
                .owner(owner)
                .title(dto.getTitle())
                .content(dto.getContent())
                .contact(dto.getContact())
                .totalMembers(dto.getTotalMembers())
                .gameDate(dto.getGameDate())
                .gender(TranslationUtil.toEnglish(genderPreference))
                .age(TranslationUtil.toEnglish(age))
                .team(TranslationUtil.toEnglish(team))
                .stadium(TranslationUtil.toEnglish(stadium))
                .createdTimeAt(LocalDateTime.now())
                .updatedTimeAt(LocalDateTime.now())
                .build();
    }

    private void updatePostFromDTO(MatePost existingPost, MateDTO dto) {
        GenderPreference genderPreference = TranslationUtil.fromKorean(dto.getGender(), GenderPreference.class);
        if (genderPreference == null) { genderPreference = GenderPreference.ANY; }

        AgeGroup age = TranslationUtil.fromKorean(dto.getAgeGroup(), AgeGroup.class);
        if (age == null) { age = AgeGroup.ANY; }

        Team team = TranslationUtil.fromKorean(dto.getTeam(), Team.class);
        if (team == null) { team = Team.ANY; }

        Stadium stadium = TranslationUtil.fromKorean(dto.getStadium(), Stadium.class);
        if (stadium == null) { stadium = Stadium.ANY; }

        existingPost.setTitle(dto.getTitle());
        existingPost.setContent(dto.getContent());
        existingPost.setContact(dto.getContact());
        existingPost.setTotalMembers(dto.getTotalMembers());
        existingPost.setGameDate(dto.getGameDate());
        existingPost.setGender(TranslationUtil.toEnglish(genderPreference));
        existingPost.setAge(TranslationUtil.toEnglish(age));
        existingPost.setTeam(TranslationUtil.toEnglish(team));
        existingPost.setStadium(TranslationUtil.toEnglish(stadium));
    }

}