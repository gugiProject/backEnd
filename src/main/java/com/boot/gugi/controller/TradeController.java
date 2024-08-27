package com.boot.gugi.controller;

import com.boot.gugi.base.dto.TradeDTO;
import com.boot.gugi.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/trade")
public class TradeController {
    private final TradeService tradeService;

    @GetMapping("/used")
    public ResponseEntity<List<TradeDTO.TradeListDTO>> getUsedPosts(){

    }

    @GetMapping("/rental")
    public ResponseEntity<List<TradeDTO.TradeListDTO>> getRentalPosts(){

    }

    @GetMapping("/search")
    public ResponseEntity<List<TradeDTO.TradeListDTO>> getSerach(@RequestParam String keyword){

    }

    @GetMapping("/{tradeId}")
    public ResponseEntity<TradeDTO.DetailTradeDTO> getDetailPost(@PathVariable UUID tradeId){

    }

    @PostMapping
    public ResponseEntity<TradeDTO.DetailTradeDTO> createPost(@RequestPart Long userId, @RequestPart TradeDTO.CreateTradeDTO createTradeDTO, @RequestPart List<MultipartFile> images){

    }

    @PatchMapping("/{tradeId}")
    public ResponseEntity<TradeDTO.DetailTradeDTO> updatePost(@RequestPart Long userId, @PathVariable UUID tradeID, @RequestPart TradeDTO.CreateTradeDTO createTradeDTO, @RequestPart List<MultipartFile> images){

    }

    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> deletePost(@RequestParam Long userId, @PathVariable UUID tradeID){

    }

    @PatchMapping("/{tradeId}/status")
    public ResponseEntity<Void> updateStatus(@RequestParam Long userId, @PathVariable UUID tradeId){

    }
}
