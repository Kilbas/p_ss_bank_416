package com.bank.account.controller;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.service.AccountDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
public class AccountDetailsController {

    private final AccountDetailsService accountDetailsService;
    private final AccountDetailsMapper accountDetailsMapper;

    @Operation(summary = "Сохранить информацию об аккаунте", description = "Возвращает сохраненую информацию об аккаунте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Информации об акканте успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, ошибка разбора JSON"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных, дублируются поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping("/")
    public ResponseEntity<AccountDetailsDTO> saveAccountDetails(@Valid @RequestBody AccountDetailsDTO accountDetailsDTO) {
        log.info("Получили информацию об аккаунте для её сохранения: {}", accountDetailsDTO.toString());
        AccountDetailsDTO savedAccountDetailsDTO = accountDetailsMapper.toDto(accountDetailsService.saveAccountDetails(accountDetailsMapper.toEntitySave(accountDetailsDTO)));
        return ResponseEntity.created(URI.create("/account/" + savedAccountDetailsDTO
                        .getId()))
                .body(savedAccountDetailsDTO);
    }

    @Operation(summary = "Обновить информацию об аккаунте", description = "Возвращает измененную информацию об аккаунте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Информации об акканте успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, ошибка разбора JSON"),
            @ApiResponse(responseCode = "404", description = "Информацию об аккаунте не найдена"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных, дублируются уникальные поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<AccountDetailsDTO> updateAccountDetails(@PathVariable Long id, @RequestBody AccountDetailsDTO accountDetailsDTO) {
        log.info("Получили информацию об аккаунте для её обновления: {}", accountDetailsDTO.toString());
        AccountDetailsDTO savedAccountDetailsDTO = accountDetailsMapper.toDto(accountDetailsService.updateAccountDetails(id, accountDetailsDTO));
        log.info("Обновлена информация об аккаунте: {}", savedAccountDetailsDTO.toString());
        return ResponseEntity.created(URI.create("/account/" + savedAccountDetailsDTO
                .getId()))
                .body(savedAccountDetailsDTO);
    }

    @Operation(summary = "Удалить информацию об аккаунте", description = "Удаляет информацию об аккаунте по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Информации об акканте успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Информацию об аккаунте не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AccountDetailsDTO> deleteAccountDetailsByID(@PathVariable Long id) {
        log.info("Получили запрос на удаление информации об аккаунте, id: {}", id);
        accountDetailsService.deleteAccountDetails(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить информацию об аккаунтах", description = "Возвращает список всей информации об аккаунтах")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список информации об аккантах успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/all")
    public ResponseEntity<List<AccountDetailsDTO>> getAccountsDetails() {
        log.info("Получили запрос на выдачу информации обо всех аккаунтах");
        List<AccountDetailsDTO> accountsDetailsDTO = accountDetailsMapper.listToDto(accountDetailsService.getAllAccountDetails());
        if (accountsDetailsDTO.isEmpty()) {
            log.warn("Нет данных, возвращать нечего");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(accountsDetailsDTO);
    }

    @Operation(summary = "Получить информацию об аккаунте по Id", description = "Возвращает список информации об аккаунте по Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информации об аккаунте успешно получена"),
            @ApiResponse(responseCode = "404", description = "Информацию об аккаунте не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/id/{id}")
    public ResponseEntity<AccountDetailsDTO> getAccountDetailsByID(@PathVariable Long id) {
        log.info("Получили запрос на выдачу информации об аакаунте по его id: {}", id);
        return ResponseEntity.ok(accountDetailsMapper.toDto(accountDetailsService.getAccountDetailsById(id)));
    }

    @Operation(summary = "Получить информацию об аккаунте по ее номеру", description = "Возвращает список информации об аккаунте по ее номеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информации об аккаунте успешно получена"),
            @ApiResponse(responseCode = "404", description = "Информацию об аккаунте не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/accountNumber/{accountNumber}")
    public ResponseEntity<AccountDetailsDTO> getAccountDetailsByAccountNumber(@PathVariable Long accountNumber) {
        log.info("Получили запрос на выдачу информации об аакаунте по номеру аккаунта: {}", accountNumber);
        return ResponseEntity.ok(accountDetailsMapper.toDto(accountDetailsService.getAccountDetailsByAccountNumber(accountNumber)));
    }

    @Operation(summary = "Получить информацию об аккаунте по Id банковской детализации", description = "Возвращает список информации об аккаунте по Id банковской детализации")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информации об акканте успешно получена"),
            @ApiResponse(responseCode = "404", description = "Информацию об аккаунте не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/bankDetailsId/{bankDetailsId}")
    public ResponseEntity<AccountDetailsDTO> getAccountDetailsByBankDetailsId(@PathVariable Long bankDetailsId) {
        log.info("Получили запрос на выдачу информации об аакаунте по его id детализации банка: {}", bankDetailsId);
        return ResponseEntity.ok(accountDetailsMapper.toDto(accountDetailsService.getAccountDetailsByBankDetailsId(bankDetailsId)));
    }
}