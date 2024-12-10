package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.service.atm.AtmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/v1/atm")
@Tag(name = "Банкоматы", description = "действия с банкоматами")
public class AtmRestController {

    private final AtmService atmService;

    public AtmRestController(AtmService atmService) {
        this.atmService = atmService;
    }

    @Operation(summary = "Получить информацию о доступных банкоматах",
            description = "Возвращает список  информации о доступных банкоматах")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список банкоматов успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping()
    public ResponseEntity<List<AtmDTO>> getAllAtm(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(atmService.getAllAtms(pageable));
    }


    @Operation(summary = "Получить информацию о банкомате по Id",
            description = "Возвращает информацию о банкомате по Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информации о банкомате успешно получена"),
            @ApiResponse(responseCode = "404", description = "Информацию о банкомате не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AtmDTO> getAtm(@PathVariable("id") Long id) {
        return  ResponseEntity.ok(atmService.getAtm(id));
    }

    @Operation(summary = "Удалить информацию о банкомате",
            description = "Удаляет информацию о банкомате по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Информации о банкомате успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Информацию о банкомате не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAtm(@PathVariable("id") Long id) {
        atmService.deleteAtm(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Сохранить информацию о банкомате",
            description = "Возвращает сохраненую информацию о банкомате")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Информации о банкомате успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping("/create")
    public ResponseEntity<AtmDTO> createAtm(@RequestBody @Valid AtmDTO atmCreateDTO) {
        return ResponseEntity.ok(atmService.addAtm(atmCreateDTO));
    }

    @Operation(summary = "Обновить информацию о банкомате",
            description = "Возвращает измененную информацию о банкомате")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Информации о банкомате успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "404", description = "Информацию о банкомате не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<AtmDTO> updateAtm(@RequestBody AtmDTO atmCreateDTO,
                                            @PathVariable Long id) {
        return ResponseEntity.ok(atmService.updateAtm(id, atmCreateDTO));
    }
}