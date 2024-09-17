package com.boot.gugi.repository;

import com.boot.gugi.model.Trade;
import com.boot.gugi.model.TradeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TradeImageRepository extends JpaRepository<TradeImage, UUID> {
    TradeImage findFirstByTradeOrderByTradeImageIdAsc(Trade trade);

    List<TradeImage> findByTrade(Trade trade);
}
