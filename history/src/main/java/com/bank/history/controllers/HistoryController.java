package com.bank.history.controllers;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.services.HistoryService;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "History API", description = "API, отвечающая за хранение идентификаторов аудитов приложения")
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping
    public ResponseEntity<HttpStatus> saveHistory(@Valid @RequestBody HistoryDTO historyDTO) {
        log.info("saveHistory: метод вызван в контроллере");
        historyService.save(historyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(HttpStatus.CREATED);
    }

    @Timed
    @GetMapping
    public Object getAllHistory(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        log.info("getAllHistory: метод вызван в контроллере");
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoryDTO> historyDTOPage = historyService.findAll(pageable);
        if (page > historyDTOPage.getTotalPages() - 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Такой страницы не существует");
        }
        return historyDTOPage;
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
