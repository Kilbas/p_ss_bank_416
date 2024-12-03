package com.bank.antifraud.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditDTO {

    private Long id; // Может быть null при создании новой записи

    @NotBlank(message = "Entity type cannot be blank")
    @Size(max = 40, message = "Entity type must not exceed 40 characters")
    private String entityType;

    @NotBlank(message = "Operation type cannot be blank")
    @Size(max = 255, message = "Operation type must not exceed 255 characters")
    private String operationType;

    @NotBlank(message = "Created by cannot be blank")
    @Size(max = 255, message = "Created by must not exceed 255 characters")
    private String createdBy;

    @Size(max = 255, message = "Modified by must not exceed 255 characters")
    private String modifiedBy;

    @NotNull(message = "Created at cannot be null")
    @PastOrPresent(message = "Created at must be in the past or present")
    private LocalDateTime createdAt;

    @NotNull(message = "Modified at cannot be null")
    @PastOrPresent(message = "Modified at must be in the past or present")
    private LocalDateTime modifiedAt;

    private String newEntityJson;

    private String entityJson;
}
