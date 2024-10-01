package com.boot.gugi.repository;

import com.boot.gugi.model.MatePost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.boot.gugi.model.QMatePost.matePost;

@Repository
public class MatePostRepositoryCustomImpl implements MatePostRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<MatePost> findByUpdatedTimeAt(LocalDateTime cursorTime, Pageable pageable) {
        return queryFactory.selectFrom(matePost)
                .where(matePost.updatedTimeAt.lt(cursorTime))
                .orderBy(matePost.updatedTimeAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
