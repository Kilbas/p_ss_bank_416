package com.bank.transfer.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Phone {
    @Id
    private long id;

    @Column
    private long number;

    @Column
    private long amount;

    @Column
    private String purpose;

    @Column
    private long accountDetailsId;

}
