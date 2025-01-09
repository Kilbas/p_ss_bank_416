package com.bank.account.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "account_details", schema = "account")
@FieldDefaults(level=AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AccountDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "passport_id", nullable = false)
    long passportId;

    @Column(name = "account_number", unique = true, nullable = false)
    long accountNumber;

    @Column(name = "bank_details_id", unique = true, nullable = false)
    long bankDetailsId;

    @Column(precision = 20, scale = 2, name = "money", nullable = false)
    BigDecimal money;

    @Column(name = "negative_balance", nullable = false)
    Boolean negativeBalance;

    @Column(name = "profile_id", nullable = false)
    long profileId;

    @Override
    public String toString() {
        return "AccountDetails{" +
                "id=" + id +
                ", passportId=" + passportId +
                ", accountNumber=" + accountNumber +
                ", bankDetailsId=" + bankDetailsId +
                ", money=" + money +
                ", negativeBalance=" + negativeBalance +
                ", profileId=" + profileId +
                '}';
    }
}
