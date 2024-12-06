package com.bank.history.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "history", schema = "history")

public class History {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "transfer_audit_id")
    private Integer transferAuditId;

    @Column(name = "profile_audit_id")
    private int profileAuditId;

    @Column(name = "account_audit_id")
    private Integer accountAuditId;

    @Column(name = "anti_fraud_audit_id")
    private Integer antiFraudAuditId;

    @Column(name = "public_bank_info_audit_id")
    private Integer publicBankInfoAuditId;

    @Column(name = "authorization_audit_id")
    private Integer authorizationAuditId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return id.equals(history.id) && Objects.equals(transferAuditId, history.transferAuditId) && profileAuditId == history.profileAuditId;
    }

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

    public int getProfileAuditId() {
        return profileAuditId;
    }

    public void setProfileAuditId(int profileAuditId) {
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
    public int hashCode() {
        return Objects.hash(id, transferAuditId, profileAuditId);
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", transferAuditId=" + transferAuditId +
                ", profileAuditId=" + profileAuditId +
                ", accountAuditId=" + accountAuditId +
                ", antiFraudAuditId=" + antiFraudAuditId +
                ", publicBankInfoAuditId=" + publicBankInfoAuditId +
                ", authorizationAuditId=" + authorizationAuditId +
                '}';
    }
}
