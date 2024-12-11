package com.bank.account.controller;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.service.AccountDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountDetailsController {

    private final AccountDetailsService accountDetailsService;
    private final String urlCreate = "/account/";

    @Operation(summary = "Сохранить информацию об аккаунте",
            description = "Возвращает сохраненую информацию об аккаунте")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Информации об акканте успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, ошибка разбора JSON"),
            @ApiResponse(responseCode = "422", description = "Ошибка целостности данных," +
                    " дублируются уникальные поля или не проходит валидация"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping("/")
    public ResponseEntity<AccountDetailsDTO> saveAccountDetails(@Valid @RequestBody AccountDetailsDTO accountDetailsDTO) {
        AccountDetailsDTO savedAccountDetailsDTO = accountDetailsService.saveAccountDetails(accountDetailsDTO);
        return ResponseEntity.created(URI.create(urlCreate + savedAccountDetailsDTO
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
        final AccountDetailsDTO savedAccountDetailsDTO = accountDetailsService.updateAccountDetails(id, accountDetailsDTO);
        return ResponseEntity.created(URI.create(urlCreate + savedAccountDetailsDTO
                .getId()))
                .body(savedAccountDetailsDTO);
    }

    @Operation(summary = "Удалить информацию об аккаунте", description = "Удаляет информацию об аккаунте по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Информации об акканте успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Информацию об аккаунте не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AccountDetailsDTO> deleteAccountDetailsByID(@PathVariable Long id) {
        accountDetailsService.deleteAccountDetails(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить информацию об аккаунтах", description = "Возвращает информацию об аккаунтах в виде списка с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список информации об аккантах успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })

    @GetMapping("/all")
    public Page<AccountDetailsDTO> getAllAccountDetails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return accountDetailsService.getAllAccountDetails(page, size);
    }

    @Operation(summary = "Получить информацию об аккаунте по Id", description = "Возвращает список информации об аккаунте по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация по идентификатору получена"),
            @ApiResponse(responseCode = "404", description = "Информация по идентификатору не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDetailsDTO> getAccountDetailsByID(@PathVariable Long id) {
        return ResponseEntity.ok(accountDetailsService.getAccountDetailsById(id));
    }

    @Operation(summary = "Получить информацию об аккаунте по ее номеру", description = "Возвращает список информации по номеру счёта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информации по номеру счёта получена"),
            @ApiResponse(responseCode = "404", description = "Информацию по номеру счёта не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDetailsDTO> getAccountDetailsByAccountNumber(@PathVariable Long accountNumber) {
        return ResponseEntity.ok(accountDetailsService.getAccountDetailsByAccountNumber(accountNumber));
    }

    @Operation(summary = "Получить информацию об аккаунте по Id банковской детализации", description = "Возвращает" +
            " список информацию по техническому идентификатору на риквизиты банка")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация по техническому идентификатору на риквизиты банка получена"),
            @ApiResponse(responseCode = "404", description = "Информацию по техническому идентификатору на риквизиты банка не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{bankDetailsId}")
    public ResponseEntity<AccountDetailsDTO> getAccountDetailsByBankDetailsId(@PathVariable Long bankDetailsId) {
        return ResponseEntity.ok(accountDetailsService.getAccountDetailsByBankDetailsId(bankDetailsId));
    }
}
