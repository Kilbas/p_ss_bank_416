package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;

import java.util.List;

public interface SuspiciousCardTransferService {
    SuspiciousCardTransferDTO findByIdCardTransfer(Long id);

    List<SuspiciousCardTransferDTO> findAllCardTransfers();

    SuspiciousCardTransferDTO createNewCardTransfer(SuspiciousCardTransferDTO transferDTO);

    SuspiciousCardTransferDTO updateCardTransfer(Long id, SuspiciousCardTransferDTO transferDTO);

    void deleteCardTransfer(Long id);

    List<SuspiciousCardTransferDTO> findTransfersByReason(String reason); // Поиск по причине

    List<SuspiciousCardTransferDTO> findBlockedTransfers();// Получить заблокированные транзакции

    List<SuspiciousCardTransferDTO> findSuspiciousTransfers(); // Получить подозрительные транзакции
}


