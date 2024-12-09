package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.service.bankDetails.BankDetailsService;
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
@RequestMapping("/v1/bankDetails")
@Tag(name = "Public-info BankDetails Controller", description = "--------")
public class BankDetailsRestController {

    private final BankDetailsService bankDetailsService;

    public BankDetailsRestController(BankDetailsService bankDetailsService) {
        this.bankDetailsService = bankDetailsService;
    }

    @Operation(summary = "Получить реквизиты всех доступных банков",
            description = "Возвращает список реквизитов всех доступных банков")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Спиок реквизитов успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping()
    public ResponseEntity<List<BankDetailsDTO>> getAllBankDetails() {
        List<BankDetailsDTO> allBankDetailsDTO = bankDetailsService.getAllBankDetails();
        return !allBankDetailsDTO.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(allBankDetailsDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Получить реквизиты банка по Id",
            description = "Возвращает реквизиты банка по Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Реквизиты банка успешно получены"),
            @ApiResponse(responseCode = "404", description = "Реквизиты банка не найдены"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BankDetailsDTO> getBankDetails(@PathVariable("id") Long id) {
        BankDetailsDTO bankDetailsDTO = bankDetailsService.getBankDetails(id);
        return bankDetailsDTO != null
                ? ResponseEntity.status(HttpStatus.OK).body(bankDetailsDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Удалить реквизиты банка",
            description = "Удаляет реквизиты банка по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Реквизиты банка успешно удалены"),
            @ApiResponse(responseCode = "404", description = "Реквизиты банка не найдены"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBankDetails(@PathVariable("id") Long id) {
        bankDetailsService.deleteBankDetail(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Сохранить реквизиты банка",
            description = "Возвращает сохраненые реквизиты банка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Реквизиты банка успешно сохранены"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "422", description = "Не коректный запрос, дублируются уникальные поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping("/create")
    public ResponseEntity<BankDetailsDTO> createBankDetails(@Valid @RequestBody BankDetailsDTO bankDetailsCreateDTO) {
        System.out.println();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bankDetailsService.addBankDetail(bankDetailsCreateDTO));
    }

    @Operation(summary = "Обновить реквизиты банка",
            description = "Возвращает измененные реквизиты банка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Реквизиты банка успешно обновлены"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "404", description = "Информацию о банкомате не найдена"),
            @ApiResponse(responseCode = "422", description = "Не коректный запрос, дублируются уникальные поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<BankDetailsDTO> updateBankDetails(@RequestBody BankDetailsDTO bankDetailsDTO,
                                                            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(bankDetailsService.updateBankDetail(id, bankDetailsDTO));
    }

}