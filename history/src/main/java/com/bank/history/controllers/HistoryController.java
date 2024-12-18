package com.bank.history.controllers;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.services.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "History API", description = "API, отвечающая за хранение идентификаторов аудитов приложения")
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> saveHistory(@Valid @RequestBody HistoryDTO historyDTO) {
        log.info("saveHistory: метод вызван в контроллере");
        historyService.save(historyDTO);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public List<HistoryDTO> getAllHistory() {
        log.info("getAllHistory: метод вызван в контроллере");
        return historyService.findAll();
    }

    @GetMapping("/{id}")
    public HistoryDTO getHistoryById(@PathVariable Long id) {
        log.info("getHistoryById: метод вызван в контроллере");
        return historyService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateHistory(@PathVariable Long id, @Valid @RequestBody HistoryDTO dto) {
        log.info("updateHistory: метод вызван в контроллере");
        historyService.update(dto, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteHistoryById(@PathVariable Long id) {
        log.info("deleteHistoryById: метод вызван в контроллере");
        historyService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
