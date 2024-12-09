package com.bank.publicinfo.controller;


import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.service.certificate.CertificateService;
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
@RequestMapping("/v1/certificate")
@Tag(name = "Public-info Certificate Controller", description = "--------")
public class CertificateRestController {

    private final CertificateService certificateService;


    public CertificateRestController(CertificateService certificateService) {
        this.certificateService = certificateService;

    }

    @Operation(summary = "Получить все сертификаты",
            description = "Возвращает список  всех сертификатов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех сертификатов успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping()
    public ResponseEntity<List<CertificateDTO>> getAllCertificate() {
        List<CertificateDTO> allCertificateDTO = certificateService.getAllCertificates();
        return !allCertificateDTO.isEmpty()
                ? ResponseEntity.status(HttpStatus.OK).body(allCertificateDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Получить сертификат по Id", description = "Возвращает сертификат по Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сертификат успешно получен"),
            @ApiResponse(responseCode = "404", description = "Сертификат не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CertificateDTO> getCertificate(@PathVariable("id") Long id) {
        CertificateDTO certificateDTO = certificateService.getCertificate(id);
        return certificateDTO != null
                ? ResponseEntity.status(HttpStatus.OK).body(certificateDTO)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @Operation(summary = "Удалить сертификат",
            description = "Удаляет сертификат по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Сертификат успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Сертификат не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable("id") Long id) {
        certificateService.deleteCertificate(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Сохранить сертификат",
            description = "Возвращает сохраненный сертификат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сертификат успешно сохранен"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping("/create")
    public ResponseEntity<CertificateDTO> createCertificate(@RequestBody @Valid CertificateDTO certificateCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(certificateService.addCertificate(certificateCreateDTO));
    }

    @Operation(summary = "Обновить сертификат",
            description = "Возвращает измененный сертификат")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Сертификат успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Не коректный запрос, проверьте заполненые поля"),
            @ApiResponse(responseCode = "404", description = "Лмцензия не найдена"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<CertificateDTO> updateCertificate(@RequestBody CertificateDTO certificateCreateDTO,
                                                            @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(certificateService.updateCertificate(id, certificateCreateDTO));
    }

}
