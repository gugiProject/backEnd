package com.boot.gugi.repository;

import com.boot.gugi.model.MatePostStatus;
import com.boot.gugi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatePostStatusRepository extends JpaRepository<MatePostStatus, Long> {
    List<MatePostStatus> findByUser(User user);
}
