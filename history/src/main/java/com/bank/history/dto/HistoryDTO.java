package com.bank.history.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor

public class HistoryDTO {

    private Long id;

    private Long transferAuditId;

    private Long profileAuditId;

    private Long accountAuditId;

    private Long antiFraudAuditId;

    private Long publicBankInfoAuditId;

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
