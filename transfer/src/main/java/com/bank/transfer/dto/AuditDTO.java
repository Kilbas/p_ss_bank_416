package com.bank.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditDTO {
    @Size(max = 40)
    private String entityType;

    @Size(max = 255)
    private String operationType;

    @Size(max = 255)
    private String createdBy;

    @Size(max = 255)
    private String modifiedBy;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private String newEntityJson;

    private String entityJson;
}
