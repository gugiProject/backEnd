package com.boot.gugi.repository;

import com.boot.gugi.base.Enum.AgeGroup;
import com.boot.gugi.base.Enum.GenderPreference;
import com.boot.gugi.base.Enum.Stadium;
import com.boot.gugi.model.MatePost;
import com.boot.gugi.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MatePostRepository extends JpaRepository<MatePost, Long>, MatePostRepositoryCustom {
    /*
    List<MatePost> findAllByConditions(
            Date date, GenderPreference gender, AgeGroup ageGroup, String team, int group, Stadium stadium, Sort sort);
     */

}
