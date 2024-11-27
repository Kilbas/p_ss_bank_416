package com.bank.transfer.repository;

import com.bank.transfer.model.CardTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferCardRepository extends JpaRepository<CardTransfer, Long> {
}
