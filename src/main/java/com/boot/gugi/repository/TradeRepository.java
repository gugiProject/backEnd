package com.boot.gugi.repository;

import com.boot.gugi.base.Enum.Category;
import com.boot.gugi.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TradeRepository extends JpaRepository<Trade, UUID> {
    Optional<Trade> findByTradeId(UUID tradeId);
    List<Trade> findByCategory(Category category);
}
