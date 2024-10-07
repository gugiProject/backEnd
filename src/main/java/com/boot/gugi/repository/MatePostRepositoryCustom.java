package com.boot.gugi.repository;

import com.boot.gugi.base.dto.MateDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatePostRepositoryCustom {

    List<MateDTO> findByUpdatedTimeAt(LocalDateTime cursorTime, Pageable pageable);
}
