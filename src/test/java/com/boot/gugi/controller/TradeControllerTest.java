package com.boot.gugi.controller;

import com.boot.gugi.base.Enum.*;
import com.boot.gugi.base.dto.TradeDTO;
import com.boot.gugi.model.Trade;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.TradeRepository;
import com.boot.gugi.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        ResultActions resultActions = mockMvc.perform(get("/api/v1/trade/used"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("대여 글 불러오기 테스트")
    void 대여글_불러오기() throws Exception{
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
        ResultActions resultActions = mockMvc.perform(get("/api/v1/trade/rental"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("검색하기 테스트")
    void 검색하기() throws Exception{
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
        ResultActions resultActions = mockMvc.perform(get("/api/v1/trade/search")
                .param("keyword", "한화"));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 상세 정보 가져오기 테스트")
    void 상품_상세_정보_가져오기() throws Exception{
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

        UUID tradeId = trade.getTradeId();

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/trade/" + tradeId));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("거래 글 작성 테스트")
    void 거래글_작성_테스트() throws Exception{
        // given
        User user = User.builder()
                .name("김야구")
                .email("gugi@gmail.com")
                .sex(Sex.MAN)
                .age(23)
                .team(Team.NONE)
                .build();

        userRepository.save(user);


        TradeDTO.CreateTradeDTO createTradeDTO = TradeDTO.CreateTradeDTO.builder()
                .category(Category.RENTAL)
                .title("Title Example")
                .content("Content Example")
                .price(1000)
                .methods(List.of(TradeMethod.MEET, TradeMethod.UNMANNED))
                .location("Example Location")
                .contact("Example Contact")
                .build();


        String shelterPostDTOJson = new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(createTradeDTO);

        MockMultipartFile tradeDTOFile = new MockMultipartFile(
                "createTradeDTO",
                "createTradeDTO",
                "application/json",
                shelterPostDTOJson.getBytes()
        );

        MockMultipartFile image1 = new MockMultipartFile(
                "images",
                "image1.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image1".getBytes()
        );

        MockMultipartFile image2 = new MockMultipartFile(
                "images",
                "image2.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "image2".getBytes()
        );

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/trade") // URL 수정
                .file(tradeDTOFile)
                .file(image1)
                .file(image2)
                .param("userId", String.valueOf(1L))
                .contentType(MediaType.MULTIPART_FORM_DATA));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("거래 수정 테스트")
    void 거래글_수정하기() throws Exception{
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

        UUID tradeId = trade.getTradeId();

        TradeDTO.CreateTradeDTO updateTradeDTO = TradeDTO.CreateTradeDTO.builder()
                .category(Category.RENTAL)
                .title("Title Example")
                .content("Content Example")
                .price(1000)
                .methods(List.of(TradeMethod.MEET, TradeMethod.UNMANNED))
                .location("Example Location")
                .contact("Example Contact")
                .build();

        String json = new ObjectMapper().writeValueAsString(updateTradeDTO);

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/trade/" + tradeId)
                .param("userId", String.valueOf(1L))
                .content(json)
                .contentType(MediaType.APPLICATION_JSON));


        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("거래 상태 변경 테스트")
    void 거래_상태_변경하기() throws Exception{
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

        UUID tradeId = trade.getTradeId();

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/v1/trade/status/" + tradeId)
                .param("userId", String.valueOf(1L)));

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("거래글 삭제 테스트")
    void 거래글_삭제하기() throws Exception{
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

        UUID tradeId = trade.getTradeId();

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/trade/" + tradeId)
                .param("userId", String.valueOf(1L)));

        // then
        resultActions.andExpect(status().isOk());
    }
}