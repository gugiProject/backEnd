package com.boot.gugi.service;

import com.boot.gugi.base.Enum.MateStatus;
import com.boot.gugi.base.dto.MateRequestDTO;
import com.boot.gugi.base.dto.MateResponseDTO;
import com.boot.gugi.base.dto.MateSearchDTO;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatePostService {

    private final MatePostRepository matePostRepository;
    private final MatePostRepositoryCustom matePostRepositoryCustom;
    private final MatePostStatusRepository matePostStatusRepository;
    private final UserRepository userRepository;
    private final MatePostApplicantRepository applicantRepository;

    UserService userService;

    @Autowired
    public MatePostService(@Qualifier("matePostRepository") MatePostRepository mateRepository,
                           @Qualifier("matePostRepositoryCustomImpl") MatePostRepositoryCustom matePostRepositoryCustom,
                           MatePostStatusRepository matePostStatusRepository,
                           UserRepository userRepository,
                           MatePostApplicantRepository applicantRepository) {
        this.matePostRepository = mateRepository;
        this.matePostRepositoryCustom = matePostRepositoryCustom;
        this.matePostStatusRepository = matePostStatusRepository;
        this.userRepository = userRepository;
        this.applicantRepository = applicantRepository;
    }

    private MatePost fromDTO(MateDTO dto, User owner) {
        return MatePost.builder()
                .owner(owner)
                .participants(dto.getParticipants())
                .totalMembers(dto.getTotalMembers())
                .gameDate(dto.getGameDate())
                .stadium(dto.getStadium())
                .title(dto.getTitle())
                .content(dto.getContent())
                .contact(dto.getContact())
                .gender(dto.getGender())
                .age(dto.getAgeGroup())
                .team(dto.getTeam())
                .createdTimeAt(LocalDateTime.now())
                .updatedTimeAt(LocalDateTime.now())
                .build();
    }

    public User getUserById(UUID ownerId) {
        return userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
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

        applicant.setStatus(MateStatus.APPROVED);
        applicantRepository.save(applicant);
        matePost.setParticipants(matePost.getParticipants() + 1);
        matePostRepository.save(matePost);

        MatePostStatus existingStatus = matePostStatusRepository.findByUserAndMatePost(applicant.getApplicant(), matePost)
                .orElseThrow(() -> new MatePostException("게시물 상태가 존재하지 않습니다."));

        existingStatus.setPostStatus(MateStatus.APPROVED);
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

    public List<MatePost> getLatestMatePosts(LocalDateTime cursorTime, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTimeAt"));

        if (cursorTime == null) {
            return matePostRepository.findAll(pageable).getContent();
        } else {
            return matePostRepositoryCustom.findByUpdatedTimeAt(cursorTime, pageable);
        }
    }

    public List<MatePost> getConditionsMatePosts(LocalDateTime cursorTime, MateSearchDTO searchCriteria, int size) {
        List<MatePost> allPosts = matePostRepository.findAll();

        return allPosts.stream()
                .map(post -> new ScoredPost(post, countMatchingConditions(post, searchCriteria), calculateScore(post, searchCriteria)))
                .collect(Collectors.groupingBy(ScoredPost::getMatchCount))
                .entrySet().stream()
                .sorted(Map.Entry.<Integer, List<ScoredPost>>comparingByKey().reversed())
                .flatMap(entry -> {
                    List<ScoredPost> scoredPosts = entry.getValue();
                    scoredPosts.sort(Comparator.comparingInt(ScoredPost::getScore).reversed());
                    return scoredPosts.stream()
                            .collect(Collectors.groupingBy(ScoredPost::getScore))
                            .values().stream()
                            .flatMap(group -> {
                                Collections.shuffle(group);
                                return group.stream();
                            });
                })
                .map(ScoredPost::getPost)
                .filter(post -> cursorTime == null || post.getUpdatedTimeAt().isBefore(cursorTime))
                .limit(size)
                .collect(Collectors.toList());
    }

    private int countMatchingConditions(MatePost post, MateSearchDTO searchCriteria) {
        int count = 0;
        if (searchCriteria.getGameDate() != null && searchCriteria.getGameDate().equals(post.getGameDate())) count++;
        if (searchCriteria.getGender() != null && searchCriteria.getGender().equals(post.getGender())) count++;
        if (searchCriteria.getAgeGroup() != null && searchCriteria.getAgeGroup().equals(post.getAge())) count++;
        if (searchCriteria.getTeam() != null && searchCriteria.getTeam().equals(post.getTeam())) count++;
        if (searchCriteria.getTotalMembers() != 0 && searchCriteria.getTotalMembers() == post.getTotalMembers()) count++;
        if (searchCriteria.getStadium() != null && searchCriteria.getStadium().equals(post.getStadium())) count++;
        return count;
    }

    private int calculateScore(MatePost post, MateSearchDTO searchCriteria) {
        int score = 0;
        if (searchCriteria.getGameDate() != null && searchCriteria.getGameDate().equals(post.getGameDate())) score += 5;
        if (searchCriteria.getGender() != null && searchCriteria.getGender().equals(post.getGender())) score += 4;
        if (searchCriteria.getAgeGroup() != null && searchCriteria.getAgeGroup().equals(post.getAge())) score += 3;
        if (searchCriteria.getTeam() != null && searchCriteria.getTeam().equals(post.getTeam())) score += 2;
        if (searchCriteria.getTotalMembers() != 0 && searchCriteria.getTotalMembers() == post.getTotalMembers()) score += 1;
        if (searchCriteria.getStadium() != null && searchCriteria.getStadium().equals(post.getStadium())) score += 1;
        return score;
    }

    private static class ScoredPost {
        private final MatePost post;
        private final int matchCount;
        private final int score;

        public ScoredPost(MatePost post, int matchCount, int score) {
            this.post = post;
            this.matchCount = matchCount;
            this.score = score;
        }

        public MatePost getPost() {
            return post;
        }

        public int getMatchCount() {
            return matchCount;
        }

        public int getScore() {
            return score;
        }
    }

    private void updatePostFromDTO(MatePost existingPost, MateDTO dto) {

        existingPost.setTotalMembers(dto.getTotalMembers());
        existingPost.setGameDate(dto.getGameDate());
        existingPost.setStadium(dto.getStadium());
        existingPost.setTitle(dto.getTitle());
        existingPost.setContent(dto.getContent());
        existingPost.setContact(dto.getContact());
        existingPost.setGender(dto.getGender());
        existingPost.setAge(dto.getAgeGroup());
        existingPost.setTeam(dto.getTeam());
    }

    public MatePost findPostById(UUID postId) {
        Optional<MatePost> optionalPost = matePostRepository.findById(postId);
        return optionalPost.orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }
}