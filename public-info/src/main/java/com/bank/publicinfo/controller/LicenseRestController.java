package com.bank.publicinfo.controller;


import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.service.license.LicenseService;
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
@RequestMapping("/v1/license")
@Tag(name = "Public-info License Controller", description = "--------")
public class LicenseRestController {

    private final LicenseService licenseService;

    public LicenseRestController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @Operation(summary = "Получить все лицензии",
            description = "Возвращает список  всех лицензий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех лицензий успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping()
    public ResponseEntity<List<LicenseDTO>> getAllLicense() {
        List<LicenseDTO> allLicenseDTO = licenseService.getAllLicenses();
        return !allLicenseDTO.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(allLicenseDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Получить лицензию по Id", description = "Возвращает лицензию по Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Лицензия успешно получена"),
            @ApiResponse(responseCode = "404", description = "Лицензия не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LicenseDTO> getLicense(@PathVariable("id") Long id) {
        LicenseDTO licenseDTO = licenseService.getLicense(id);
        return licenseDTO != null
                ? ResponseEntity.status(HttpStatus.OK).body(licenseDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Удалить лицензию",
            description = "Удаляет лицензию по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Лицензия успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Лицензия не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable("id") Long id) {
        licenseService.deleteLicense(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Сохранить лицензию",
            description = "Возвращает сохраненую лицензию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Лицензия успешно сохранена"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping("/create")
    public ResponseEntity<LicenseDTO> createLicense(@RequestBody @Valid LicenseDTO licenseCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(licenseService.addLicense(licenseCreateDTO));
    }

    @Operation(summary = "Обновить лицензию",
            description = "Возвращает измененную лицензию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Лицензия успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "404", description = "Лмцензия не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<LicenseDTO> updateLicense(@RequestBody LicenseDTO licenseCreateDTO,
                                                    @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(licenseService.updateLicense(id, licenseCreateDTO));
    }

}
