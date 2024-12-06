package com.bank.history.dto;

import lombok.NoArgsConstructor;
import java.util.Objects;

@NoArgsConstructor

public class HistoryDTO {

    private Integer id;

    private Integer transferAuditId;

    private Integer profileAuditId;

    private Integer accountAuditId;

    private Integer antiFraudAuditId;

    private Integer publicBankInfoAuditId;

    private Integer authorizationAuditId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTransferAuditId() {
        return transferAuditId;
    }

    public void setTransferAuditId(Integer transferAuditId) {
        this.transferAuditId = transferAuditId;
    }

    public Integer getProfileAuditId() {
        return profileAuditId;
    }

    public void setProfileAuditId(Integer profileAuditId) {
        this.profileAuditId = profileAuditId;
    }

    public Integer getAccountAuditId() {
        return accountAuditId;
    }

    public void setAccountAuditId(Integer accountAuditId) {
        this.accountAuditId = accountAuditId;
    }

    public Integer getAntiFraudAuditId() {
        return antiFraudAuditId;
    }

    public void setAntiFraudAuditId(Integer antiFraudAuditId) {
        this.antiFraudAuditId = antiFraudAuditId;
    }

    public Integer getPublicBankInfoAuditId() {
        return publicBankInfoAuditId;
    }

    public void setPublicBankInfoAuditId(Integer publicBankInfoAuditId) {
        this.publicBankInfoAuditId = publicBankInfoAuditId;
    }

    public Integer getAuthorizationAuditId() {
        return authorizationAuditId;
    }

    public void setAuthorizationAuditId(Integer authorizationAuditId) {
        this.authorizationAuditId = authorizationAuditId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryDTO that = (HistoryDTO) o;
        return transferAuditId.equals(that.transferAuditId) && profileAuditId.equals(that.profileAuditId) && accountAuditId == that.accountAuditId;
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
