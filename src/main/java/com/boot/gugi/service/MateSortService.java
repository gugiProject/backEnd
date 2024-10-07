package com.boot.gugi.service;

import com.boot.gugi.base.dto.MateDTO;
import com.boot.gugi.base.dto.MateSearchDTO;
import com.boot.gugi.model.MatePost;
import com.boot.gugi.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MateSortService {

    private final MatePostRepository matePostRepository;
    private final MatePostRepositoryCustom matePostRepositoryCustom;

    @Autowired
    public MateSortService(@Qualifier("matePostRepository") MatePostRepository mateRepository,
                           @Qualifier("matePostRepositoryCustomImpl") MatePostRepositoryCustom matePostRepositoryCustom) {
        this.matePostRepository = mateRepository;
        this.matePostRepositoryCustom = matePostRepositoryCustom;
    }

    public List<MateDTO> getLatestMatePosts(LocalDateTime cursorTime, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "updatedTimeAt"));

        if (cursorTime == null) {
            return matePostRepository.findAll(pageable).getContent().stream()
                    .map(post -> MateDTO.builder()
                            .title(post.getTitle())
                            .content(post.getContent())
                            .contact(post.getContact())
                            .totalMembers(post.getTotalMembers())
                            .gameDate(post.getGameDate())
                            .gender(post.getGender())
                            .ageGroup(post.getAge())
                            .team(post.getTeam())
                            .stadium(post.getStadium())
                            .ownerProfileImg(post.getOwner().getProfileImg())
                            .build())
                    .collect(Collectors.toList());
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
}
