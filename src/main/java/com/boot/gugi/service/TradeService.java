package com.boot.gugi.service;

import com.boot.gugi.base.dto.TradeDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeService {
    public List<TradeDTO.TradeListDTO> getUsedPosts(){
        List<TradeDTO.TradeListDTO> result = new ArrayList<>();
        return result;
    }

    public List<TradeDTO.TradeListDTO> getRentalPosts(){
        List<TradeDTO.TradeListDTO> result = new ArrayList<>();
        return result;
    }

    public List<TradeDTO.TradeListDTO> getSearch(String keyword){
        List<TradeDTO.TradeListDTO> result = new ArrayList<>();
        return result;
    }

    public TradeDTO.DetailTradeDTO getDetailPost(UUID tradeId){
        TradeDTO.DetailTradeDTO dto = new TradeDTO.DetailTradeDTO();
        return dto;
    }

    public TradeDTO.DetailTradeDTO createPost(Long userId, TradeDTO.CreateTradeDTO createTradeDTO){
        TradeDTO.DetailTradeDTO dto = new TradeDTO.DetailTradeDTO();
        return dto;
    }

    public TradeDTO.DetailTradeDTO updatePost(Long userId, TradeDTO.CreateTradeDTO createTradeDTO){
        TradeDTO.DetailTradeDTO dto = new TradeDTO.DetailTradeDTO();
        return dto;
    }

    public void deletePost(Long userId, UUID tradeId){

    }

    public void updateStatus(Long userId, UUID tradeId){

    }
}
