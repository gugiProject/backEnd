package com.boot.gugi.controller;

import com.boot.gugi.base.dto.TradeDTO;
import com.boot.gugi.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
        List<TradeDTO.TradeListDTO> result = tradeService.getUsedPosts();

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/rental")
    public ResponseEntity<List<TradeDTO.TradeListDTO>> getRentalPosts(){
        List<TradeDTO.TradeListDTO> result = tradeService.getRentalPosts();

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TradeDTO.TradeListDTO>> getSearch(@RequestParam String keyword){
        List<TradeDTO.TradeListDTO> result = tradeService.getSearch(keyword);

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity<TradeDTO.DetailTradeDTO> getDetailPost(@PathVariable UUID tradeId){
        TradeDTO.DetailTradeDTO result = tradeService.getDetailPost(tradeId);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<UUID> createPost(@RequestParam Long userId, @RequestPart TradeDTO.CreateTradeDTO createTradeDTO, @RequestPart List<MultipartFile> images){
        UUID result = tradeService.createPost(userId, createTradeDTO);
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/{tradeId}")
    public ResponseEntity<UUID> updatePost(@RequestParam Long userId, @PathVariable UUID tradeId, @RequestBody TradeDTO.CreateTradeDTO updateTradeDTO){
        UUID result = tradeService.updatePost(userId, tradeId, updateTradeDTO);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> deletePost(@RequestParam Long userId, @PathVariable UUID tradeId){
        tradeService.deletePost(userId, tradeId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/status/{tradeId}")
    public ResponseEntity<Void> updateStatus(@RequestParam Long userId, @PathVariable UUID tradeId){
        tradeService.updateStatus(userId, tradeId);
        return ResponseEntity.ok().build();
    }
}
