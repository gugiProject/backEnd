package com.boot.gugi.repository;

import com.boot.gugi.model.MatePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MatePostRepository extends JpaRepository<MatePost, UUID>, MatePostRepositoryCustom {
    /*
    List<MatePost> findAllByConditions(
            Date date, GenderPreference gender, AgeGroup ageGroup, String team, int group, Stadium stadium, Sort sort);
     */
}
