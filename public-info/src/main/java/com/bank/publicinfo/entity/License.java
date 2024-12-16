package com.bank.publicinfo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "license", schema = "public_bank_information")
public class License {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "photo", nullable = false)
    private byte[] photo;

    @NotNull
    @JsonIgnore
    @Fetch(FetchMode.JOIN)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_details_id", referencedColumnName = "id")
    private BankDetails bankDetailsLicense;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        License license = (License) o;
        if (license.getId()==null) return false;
        return Objects.equals(id, license.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}