package com.boot.gugi.repository;

import com.boot.gugi.model.MatePost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.boot.gugi.model.QMatePost.matePost;

@Repository
public class MatePostRepositoryCustomImpl implements MatePostRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<MatePost> findByUpdatedTimeAt(LocalDateTime cursorTime, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MatePost> query = cb.createQuery(MatePost.class);
        Root<MatePost> root = query.from(MatePost.class);

        query.select(root)
                .where(cb.lessThan(root.get("updateTimeAt"), cursorTime))
                .orderBy(cb.desc(root.get("updateTimeAt")));

        return entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
