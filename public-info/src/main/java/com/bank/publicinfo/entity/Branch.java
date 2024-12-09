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
import java.time.LocalTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "branch", schema = "public_bank_information")
public class Branch {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 370)
    @Column(name = "address", length = 370, nullable = false)
    private String address;

    @NotNull
    @Column(name = "phone_number", unique = true, nullable = false)
    private Long phoneNumber;

    @NotNull
    @Size(max = 250)
    @Column(name = "city", length = 250, nullable = false)
    private String city;

    @NotNull
    @Column(name = "start_of_work", nullable = false)
    private LocalTime startOfWork;

    @NotNull
    @Column(name = "end_of_work", nullable = false)
    private LocalTime endOfWork;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "branch", fetch = FetchType.LAZY)
    private Set<Atm> atms;

}
