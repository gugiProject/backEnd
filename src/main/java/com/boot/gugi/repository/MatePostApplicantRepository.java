package com.boot.gugi.repository;

import com.boot.gugi.model.MatePost;
import com.boot.gugi.model.MatePostApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatePostApplicantRepository extends JpaRepository<MatePostApplicant, Long> {
    Optional<MatePostApplicant> findByApplicantIdAndMatePostId(Long userId, Long postId);
}