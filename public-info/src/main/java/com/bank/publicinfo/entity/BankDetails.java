package com.bank.publicinfo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Entity
@Table(name = "bank_details", schema = "public_bank_information")
public class BankDetails {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bik", unique = true, nullable = false)
    private Long bik;

    @NotNull
    @Column(name = "inn", unique = true, nullable = false)
    private Long inn;

    @NotNull
    @Column(name = "kpp", unique = true, nullable = false)
    private Long kpp;

    @NotNull
    @Column(name = "cor_account", unique = true, nullable = false)
    private Integer corAccount;

    @NotNull
    @Size(max = 180)
    @Column(name = "city", length = 180, nullable = false)
    private String city;

    @NotNull
    @Size(max = 15)
    @Column(name = "joint_stock_company", length = 15, nullable = false)
    private String jointStockCompany;

    @NotNull
    @Size(max = 80)
    @Column(name = "name", length = 80, nullable = false)
    private String name;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankDetailsLicense", fetch = FetchType.LAZY)
    private Set<License> licenses;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.JOIN)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bankDetailsCertificate", fetch = FetchType.LAZY)
    private Set<Certificate> certificates;
}