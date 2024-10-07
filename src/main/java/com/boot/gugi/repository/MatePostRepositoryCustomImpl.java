package com.boot.gugi.repository;

import com.boot.gugi.model.MatePost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import com.boot.gugi.base.dto.MateDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.boot.gugi.model.QMatePost.matePost;
import static com.boot.gugi.model.QUser.user;

@Repository
public class MatePostRepositoryCustomImpl implements MatePostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MatePostRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<MateDTO> findByUpdatedTimeAt(LocalDateTime cursorTime, Pageable pageable) {
        List<MatePost> posts = queryFactory.selectFrom(matePost)
                .join(matePost.owner, user)
                .where(matePost.updatedTimeAt.lt(cursorTime))
                .orderBy(matePost.updatedTimeAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return posts.stream()
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
    }
}