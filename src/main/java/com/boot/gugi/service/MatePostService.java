package com.boot.gugi.service;

import com.boot.gugi.base.Enum.MateStatus;
import com.boot.gugi.base.dto.MateSearchDTO;
import com.boot.gugi.model.MatePostStatus;
import com.boot.gugi.model.MatePost;
import com.boot.gugi.base.dto.MateDTO;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.MatePostRepository;
import com.boot.gugi.repository.MatePostRepositoryCustom;
import com.boot.gugi.repository.MatePostStatusRepository;
import com.boot.gugi.repository.UserRepository;
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
    UserService userService;

    @Autowired
    public MatePostService(@Qualifier("matePostRepository") MatePostRepository mateRepository,
                           @Qualifier("matePostRepositoryCustomImpl") MatePostRepositoryCustom matePostRepositoryCustom,
                           MatePostStatusRepository matePostStatusRepository,
                           UserRepository userRepository) {
        this.matePostRepository = mateRepository;
        this.matePostRepositoryCustom = matePostRepositoryCustom;
        this.matePostStatusRepository = matePostStatusRepository;
        this.userRepository = userRepository;
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

    public User getUserById(Long ownerId) {
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

    public MatePost updateMatePost(Long postId, MateDTO dto) {
        MatePost existingPost = findPostById(postId);
        updatePostFromDTO(existingPost, dto);
        existingPost.setUpdatedTimeAt(LocalDateTime.now());

        return matePostRepository.save(existingPost);
    }

    public List<MatePost> getLatestMatePosts(Long cursorId, int size) {
        LocalDateTime cursorTime = null;

        if (cursorId != null) {
            MatePost cursorPost = matePostRepository.findById(cursorId).orElse(null);
            if (cursorPost != null) {
                cursorTime = cursorPost.getUpdatedTimeAt();
            }
        }
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updateTimeAt"));

        if (cursorTime == null) {
            return matePostRepository.findAll(pageable).getContent();
        } else {
            return matePostRepositoryCustom.findByUpdatedTimeAt(cursorTime, pageable);
        }
    }

    public List<MatePost> getConditionsMatePosts(Long cursorId, MateSearchDTO searchCriteria, int size) {
        Optional<LocalDateTime> cursorTime;

        if (cursorId != null) {
            cursorTime = matePostRepository.findById(cursorId)
                    .map(MatePost::getUpdatedTimeAt);
        } else {
            cursorTime = Optional.empty();
        }

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
                .filter(post -> cursorTime.map(time -> post.getUpdatedTimeAt().isBefore(time)).orElse(true))
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

    public MatePost findPostById(Long postId) {
        Optional<MatePost> optionalPost = matePostRepository.findById(postId);
        return optionalPost.orElseThrow(() -> new RuntimeException("Post not found with id: " + postId));
    }
}