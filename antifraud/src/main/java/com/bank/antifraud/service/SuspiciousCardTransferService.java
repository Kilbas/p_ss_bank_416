package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;

import java.util.List;

public interface SuspiciousCardTransferService {
    SuspiciousCardTransferDTO findById(Long id);

    List<SuspiciousCardTransferDTO> findAll();

    SuspiciousCardTransferDTO create(SuspiciousCardTransferDTO transferDTO);

    SuspiciousCardTransferDTO update(Long id, SuspiciousCardTransferDTO transferDTO);

    void delete(Long id);

    List<SuspiciousCardTransferDTO> findBlockedTransfers(); // Получить заблокированные транзакции
}
