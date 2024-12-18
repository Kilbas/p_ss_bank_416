package com.bank.history.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryDTO that = (HistoryDTO) o;
        return transferAuditId.equals(that.transferAuditId) && profileAuditId.equals(that.profileAuditId) && accountAuditId.equals(that.accountAuditId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferAuditId, profileAuditId, accountAuditId);
    }

    @Override
    public String toString() {
        return "HistoryDTO{" +
                "transferAuditId=" + transferAuditId +
                ", profileAuditId=" + profileAuditId +
                ", accountAuditId=" + accountAuditId +
                ", antiFraudAuditId=" + antiFraudAuditId +
                ", publicBankInfoAuditId=" + publicBankInfoAuditId +
                ", authorizationAuditId=" + authorizationAuditId +
                '}';
    }
}
