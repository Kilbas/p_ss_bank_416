package com.bank.transfer.controller;

import com.bank.transfer.dto.PhoneTransferDTO;
import com.bank.transfer.mapper.PhoneTransferMapper;
import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.service.TransferPhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/phone")
@Tag(name = "Transfer Phone Controller", description = "Управление переводами, связанными с телефонными платежами")
public class TransferPhoneRestController {
    private final TransferPhoneService transferPhoneService;
    private final PhoneTransferMapper phoneTransferMapper;

    @Operation(summary = "Получить все переводы по телефонам", description = "Возвращает список всех операций перевода, связанных с телефонными платежами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список переводов успешно получен"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @GetMapping
    public ResponseEntity<List<PhoneTransferDTO>> getPhoneTransfers() {
        List<PhoneTransfer> phoneTransfers = transferPhoneService.getAllPhoneTransfers();
        List<PhoneTransferDTO> phoneTransferDTOs = phoneTransfers.stream()
                .map(phoneTransferMapper::phoneTransferToDTO)
                .toList();

        return ResponseEntity.ok(phoneTransferDTOs);
    }

    @Operation(summary = "Получить перевод по ID", description = "Возвращает данные о переводе на основании указанного идентификатора")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные перевода успешно получены"),
            @ApiResponse(responseCode = "404", description = "Перевод с указанным ID не найден")
    })
    @GetMapping("{id}")
    public ResponseEntity<PhoneTransferDTO> getPhoneTransfer(@PathVariable Long id) {
        PhoneTransfer phoneTransfer = transferPhoneService.getPhoneTransferById(id);
        PhoneTransferDTO phoneTransferDTO = phoneTransferMapper.phoneTransferToDTO(phoneTransfer);

        return ResponseEntity.ok(phoneTransferDTO);
    }

    @Operation(summary = "Создать новый перевод", description = "Добавляет новый перевод по телефону в базу данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для создания перевода"),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере")
    })
    @PostMapping
    public ResponseEntity<PhoneTransferDTO> createPhoneTransfer(@Valid @RequestBody PhoneTransferDTO phoneTransferDTO) {
        PhoneTransfer phoneTransfer = phoneTransferMapper.dtoToPhoneTransfer(phoneTransferDTO);
        transferPhoneService.addPhoneTransfer(phoneTransfer);

        return ResponseEntity.ok(phoneTransferDTO);
    }

    @Operation(summary = "Обновить перевод", description = "Обновляет данные перевода по телефону на основании указанного ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Перевод успешно обновлён"),
            @ApiResponse(responseCode = "404", description = "Перевод с указанным ID не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные для обновления перевода")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PhoneTransferDTO> updatePhoneTransfer(@Valid @RequestBody PhoneTransferDTO phoneTransferDTO, @PathVariable long id) {
        PhoneTransfer phoneTransfer = phoneTransferMapper.dtoToPhoneTransfer(phoneTransferDTO);
        PhoneTransfer updatedPhoneTransfer = transferPhoneService.updatePhoneTransfer(phoneTransfer, id);
        PhoneTransferDTO updatedPhoneTransferDTO = phoneTransferMapper.phoneTransferToDTO(updatedPhoneTransfer);

        return ResponseEntity.ok(updatedPhoneTransferDTO);
    }

    @Operation(summary = "Удалить перевод", description = "Удаляет перевод по телефону на основании указанного ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Перевод успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Перевод с указанным ID не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<PhoneTransfer> deletePhoneTransfer(@PathVariable long id) {
        transferPhoneService.deletePhoneTransfer(id);

        return ResponseEntity.noContent().build();
    }
}
