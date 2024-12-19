package com.bank.history.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString

public class HistoryDTO {

    private Long id;

    @Min(value = 1, message = "transferAuditId: значение не может быть меньше 1")
    private Long transferAuditId;

    @Min(value = 1, message = "profileAuditId: значение не может быть меньше 1")
    private Long profileAuditId;

    @Min(value = 1, message = "accountAuditId: значение не может быть меньше 1")
    private Long accountAuditId;

    @Min(value = 1, message = "antiFraudAuditId: значение не может быть меньше 1")
    private Long antiFraudAuditId;

    @Min(value = 1, message = "publicBankInfoAuditId: значение не может быть меньше 1")
    private Long publicBankInfoAuditId;

    @Min(value = 1, message = "authorizationAuditId: значение не может быть меньше 1")
    private Long authorizationAuditId;

}
