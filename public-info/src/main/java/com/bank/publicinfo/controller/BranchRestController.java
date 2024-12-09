package com.bank.publicinfo.controller;


import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.service.branch.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/branch")
@Tag(name = "Public-info Branch Controller", description = "--------")

public class BranchRestController {

    private final BranchService branchService;

    public BranchRestController(BranchService branchService) {
        this.branchService = branchService;
    }

    @Operation(summary = "Получить информацию обо всех доступных отделений банков",
            description = "Возвращает список информации обо всех доступных отделений банков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Спиок отделений банков успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping()
    public ResponseEntity<List<BranchDTO>> getAllBranch() {
        List<BranchDTO> allBranchDTO = branchService.getAllBranches();
        return !allBranchDTO.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(allBranchDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Получить информацию об отделении банка по Id",
            description = "Возвращает информацию об отделении банка по Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Отделение банка успешно получено"),
            @ApiResponse(responseCode = "404", description = "Отделение банка не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranch(@PathVariable("id") Long id) {
        BranchDTO branchDTO = branchService.getBranch(id);
        return branchDTO != null
                ? ResponseEntity.status(HttpStatus.OK).body(branchDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Удалить информацию об отделении банка",
            description = "Удаляет информацию об отделении банка по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Отделение банка успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Отделение банка не найдено"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable("id") Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Сохранить информацию об отделении банка",
            description = "Возвращает информацию об отделении банка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отделение банка успешно сохранено"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "422", description = "Не коректный запрос, дублируются уникальные поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping("/create")
    public ResponseEntity<BranchDTO> createBranch(@RequestBody @Valid BranchDTO branchCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(branchService.addBranch(branchCreateDTO));
    }

    @Operation(summary = "Обновить информацию об отделении банка",
            description = "Возвращает информацию об отделении банка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отделение банка успешно обновлено"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "404", description = "Информации о отделении банка не найдена"),
            @ApiResponse(responseCode = "422", description = "Не коректный запрос, дублируются уникальные поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<BranchDTO> updateBranch(@RequestBody BranchDTO branchCreateDTO,
                                            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(branchService.updateBranch(id, branchCreateDTO));
    }
}
