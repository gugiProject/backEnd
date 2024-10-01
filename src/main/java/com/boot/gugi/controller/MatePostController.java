package com.boot.gugi.controller;

import com.boot.gugi.base.dto.MateRequestDTO;
import com.boot.gugi.base.dto.MateResponseDTO;
import com.boot.gugi.model.MatePost;
import com.boot.gugi.base.dto.MateDTO;
import com.boot.gugi.base.dto.MateSearchDTO;
import com.boot.gugi.model.User;
import com.boot.gugi.service.MatePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mate")
public class MatePostController {

    @Autowired
    private MatePostService mateService;

    @PostMapping
    public ResponseEntity<MateDTO> createMatePost(
            @RequestHeader("User-ID") Long ownerId,
            @RequestBody MateDTO mateDTO) {

        User owner = mateService.getUserById(ownerId);

        MatePost createdPost = mateService.createMatePost(mateDTO, owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost.toDTO());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateMatePost(@PathVariable Long id, @RequestBody MateDTO mateDTO) {
        mateService.updateMatePost(id, mateDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/latest")
    public ResponseEntity<List<MatePost>> latestSortMatePost(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(required = false, defaultValue = "15") int size) {
        List<MatePost> posts = mateService.getLatestMatePosts(cursorId, size);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/conditions")
    public ResponseEntity<List<MatePost>> conditionSortMatePost(
            @RequestParam(required = false) Long cursorId,
            @RequestBody MateSearchDTO searchCriteria,
            @RequestParam(defaultValue = "15") int size) {

        List<MatePost> posts = mateService.getConditionsMatePosts(cursorId, searchCriteria, size);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/apply")
    public ResponseEntity<Void> applyForMatePost(@RequestBody MateRequestDTO mateRequestDTO) {
        mateService.applyForMatePost(mateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/approve")
    public ResponseEntity<Void> approveApplication(@RequestBody MateResponseDTO response) {
        mateService.approveApplication(response);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/reject")
    public ResponseEntity<Void> rejectApplication(@RequestBody MateResponseDTO response) {
        mateService.rejectApplication(response);
        return ResponseEntity.ok().build();
    }
}