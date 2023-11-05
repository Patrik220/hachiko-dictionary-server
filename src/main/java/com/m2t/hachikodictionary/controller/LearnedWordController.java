package com.m2t.hachikodictionary.controller;

import com.m2t.hachikodictionary.dto.Response;
import com.m2t.hachikodictionary.dto.UpdateLearnedWordRequest;
import com.m2t.hachikodictionary.exception.AccountNotFoundException;
import com.m2t.hachikodictionary.exception.WordNotFoundException;
import com.m2t.hachikodictionary.service.LearnedWordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/learned-word")
@Validated
public class LearnedWordController {

    private final LearnedWordService learnedWordService;

    public LearnedWordController(LearnedWordService learnedWordService) {
        this.learnedWordService = learnedWordService;
    }

    @PutMapping("/update")
    public ResponseEntity<Response> updateLearnedWord(@RequestBody @Valid UpdateLearnedWordRequest updateLearnedWordRequest) {
        try {
            return ResponseEntity.ok(learnedWordService.updateLearnedWord(updateLearnedWordRequest));
        } catch (AccountNotFoundException | WordNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new Response(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Response(false, e.getMessage()));
        }
    }
}
