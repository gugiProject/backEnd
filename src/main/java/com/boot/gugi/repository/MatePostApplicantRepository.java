package com.boot.gugi.repository;

import com.boot.gugi.model.MatePost;
import com.boot.gugi.model.MatePostApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatePostApplicantRepository extends JpaRepository<MatePostApplicant, UUID> {
    Optional<MatePostApplicant> findByApplicantIdAndMatePostId(UUID userId, UUID postId);
    List<MatePostApplicant> findByOwnerId(UUID ownerId);
    List<MatePostApplicant> findByApplicantId(UUID applicantId);
}