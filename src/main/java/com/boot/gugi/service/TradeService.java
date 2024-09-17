package com.boot.gugi.service;

import com.boot.gugi.base.Enum.Category;
import com.boot.gugi.base.Enum.TradeStatus;
import com.boot.gugi.base.dto.TradeDTO;
import com.boot.gugi.model.Trade;
import com.boot.gugi.model.TradeImage;
import com.boot.gugi.model.User;
import com.boot.gugi.repository.TradeImageRepository;
import com.boot.gugi.repository.TradeRepository;
import com.boot.gugi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeImageRepository tradeImageRepository;
    private final UserRepository userRepository;

    public List<TradeDTO.TradeListDTO> getUsedPosts(){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("tradeId"));
        List<Trade> lists = tradeRepository.findByCategory(Category.USED);
        return lists.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
    }

    public List<TradeDTO.TradeListDTO> getRentalPosts(){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("tradeId"));
        List<Trade> lists = tradeRepository.findByCategory(Category.RENTAL);
        return lists.stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList());
    }

    private TradeDTO.TradeListDTO convertToListDTO(Trade tradePost) {
        TradeDTO.TradeListDTO dto = new TradeDTO.TradeListDTO();
        dto.setTradeId(tradePost.getTradeId());
        dto.setTitle(tradePost.getTitle());
        dto.setPrice(tradePost.getPrice());
        dto.setLocation(tradePost.getLocation());
        dto.setTradeStatus(tradePost.getStatus());
        dto.setTradeMethod(tradePost.getMethods());
//        dto.setThumbnail(tradeImageRepository.findFirstByTradeOrderByTradeImageIdAsc(tradePost).getImageUrl());
        return dto;
    }

    public List<TradeDTO.TradeListDTO> getSearch(String keyword){
        List<TradeDTO.TradeListDTO> result = new ArrayList<>();
        return result;
    }

    public TradeDTO.DetailTradeDTO getDetailPost(UUID tradeId){
        Trade trade = tradeRepository.findByTradeId(tradeId).orElseThrow();
        TradeDTO.DetailTradeDTO dto = new TradeDTO.DetailTradeDTO();
        dto.setUserId(trade.getUser().getUserId());
        dto.setCategory(trade.getCategory());
        dto.setTitle(trade.getTitle());
        dto.setContent(trade.getContent());
        dto.setPrice(trade.getPrice());
        dto.setStatus(trade.getStatus());
        dto.setMethods(trade.getMethods());
        dto.setLocation(trade.getLocation());
        dto.setContact(trade.getContact());

//        List<TradeImage> tradeImages = tradeImageRepository.findByTrade(trade);
//        List<String> images = new ArrayList<>();
//        for (int i=0; i<tradeImages.size(); i++) {
//            images.add(tradeImages.get(i).getImageUrl());
//        }
//        dto.setImages(images);

        return dto;
    }

    public UUID createPost(Long userId, TradeDTO.CreateTradeDTO createTradeDTO){
        User user = userRepository.findByUserId(userId).orElseThrow();
        Trade trade = Trade.builder()
                .user(user)
                .title(createTradeDTO.getTitle())
                .content(createTradeDTO.getTitle())
                .category(createTradeDTO.getCategory())
                .price(createTradeDTO.getPrice())
                .methods(createTradeDTO.getMethods())
                .location(createTradeDTO.getLocation())
                .contact(createTradeDTO.getContact())
                .build();

        tradeRepository.save(trade);

        return trade.getTradeId();
    }

    public UUID updatePost(Long userId, UUID tradeId, TradeDTO.CreateTradeDTO createTradeDTO){
        User user = userRepository.findByUserId(userId).orElseThrow();
        Trade trade = tradeRepository.findByTradeId(tradeId).orElseThrow();

        if(!user.equals(trade.getUser())){
            throw new RuntimeException();
        }

        trade.setTitle(createTradeDTO.getTitle());
        trade.setContent(createTradeDTO.getTitle());
        trade.setCategory(createTradeDTO.getCategory());
        trade.setPrice(createTradeDTO.getPrice());
        trade.setMethods(createTradeDTO.getMethods());
        trade.setLocation(createTradeDTO.getLocation());
        trade.setContact(createTradeDTO.getContact());

        tradeRepository.save(trade);
        return trade.getTradeId();
    }

    public void deletePost(Long userId, UUID tradeId){
        User user = userRepository.findByUserId(userId).orElseThrow();
        Trade trade = tradeRepository.findByTradeId(tradeId).orElseThrow();

        if(!user.equals(trade.getUser())){
            throw new RuntimeException();
        }

        tradeRepository.delete(trade);
    }

    public void updateStatus(Long userId, UUID tradeId){
        User user = userRepository.findByUserId(userId).orElseThrow();
        Trade trade = tradeRepository.findByTradeId(tradeId).orElseThrow();

        if(!user.equals(trade.getUser())){
            throw new RuntimeException();
        }

        trade.setStatus(TradeStatus.DONE);
        tradeRepository.save(trade);
    }
}
