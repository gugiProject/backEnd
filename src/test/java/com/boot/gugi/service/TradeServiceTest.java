package com.boot.gugi.service;

import com.boot.gugi.controller.TradeController;
import com.boot.gugi.repository.TradeRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class TradeServiceTest {
    @Autowired
    TradeService tradeService;

    @Autowired
    TradeRepository tradeRepository;

}