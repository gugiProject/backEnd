package com.boot.gugi.repository;

import com.boot.gugi.model.MatePost;
import com.boot.gugi.model.MatePostStatus;
import com.boot.gugi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatePostStatusRepository extends JpaRepository<MatePostStatus, UUID> {
    List<MatePostStatus> findByUser(User user);
    Optional<MatePostStatus> findByUserAndMatePost(User user, MatePost matePost);
}
