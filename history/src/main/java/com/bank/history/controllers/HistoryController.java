package com.bank.history.controllers;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/history")
@RestController
public class HistoryController {

    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    public ResponseEntity<HttpStatus> saveHistory(@RequestBody HistoryDTO historyDTO) {
        historyService.save(historyDTO);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @GetMapping
    public List<HistoryDTO> getAllHistory() {
        return historyService.findAll();
    }

    @GetMapping("/{id}")
    public HistoryDTO getHistoryById(@PathVariable int id) {
        return historyService.findById(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateHistory(@PathVariable int id, @RequestBody HistoryDTO dto) {
        historyService.update(dto, id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteHistoryById(@PathVariable int id) {
        historyService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
