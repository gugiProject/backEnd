package com.boot.gugi.controller;

import com.boot.gugi.base.Enum.*;
import com.boot.gugi.model.Trade;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.TradeRepository;
import com.boot.gugi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static com.boot.gugi.base.Enum.Sex.MAN;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TradeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("중고거래 글 불러오기 테스트")
    void 중고글_불러오기() throws Exception {
        // given
        User user = User.builder()
                .name("김야구")
                .email("gugi@gmail.com")
                .sex(Sex.MAN)
                .age(23)
                .team(Team.NONE)
                .build();

        userRepository.save(user);

        Trade trade = Trade.builder()
                .user(user)
                .category(Category.RENTAL)
                .title("title1")
                .content("content1")
                .price(1000)
                .status(TradeStatus.ING)
                .methods(new ArrayList<>(TradeMethod.MEET.ordinal()))
                .location("잠실역 2번출구")
                .contact("contract")
                .build();

        tradeRepository.save(trade);

        // when
        ResultActions resultActions = mockMvc.perform(get("/trade"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("대여 글 불러오기 테스트")
    void 대여글_불러오기(){

    }
}