package com.bank.publicinfo.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class AuditDTO {

    private Long id;

    @NotNull(message = "Вы не указали entityType")
    @Size(min = 1, max = 40, message = "Вы вышли за допустимый диапозо от 1 до 40 ")
    private String entityType;

    @NotNull(message = "Вы не указали operationType")
    private String operationType;

    @NotNull(message = "Вы не указали createdBy")
    private String createdBy;

    private String modifiedBy;

    @NotNull(message = "Вы не указали createdAt")
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String newEntityJson;

    @NotNull(message = "Вы не указали entityJson")
    private String entityJson;
}