package com.bank.transfer.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Card {
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
