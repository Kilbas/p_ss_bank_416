package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;

import java.util.List;

public interface SuspiciousPhoneTransfersService {
    SuspiciousPhoneTransferDTO findByIdPhoneTransfers(Long id);

    List<SuspiciousPhoneTransferDTO> findAllPhoneTransfers();

    SuspiciousPhoneTransferDTO createNewPhoneTransfers(SuspiciousPhoneTransferDTO dto);

    SuspiciousPhoneTransferDTO updatePhoneTransfers(Long id, SuspiciousPhoneTransferDTO dto);

    void deletePhoneTransfers(Long id);

    List<SuspiciousPhoneTransferDTO> findTransfersByReason(String reason); // Поиск по причине

    List<SuspiciousPhoneTransferDTO> findBlockedTransfers();// Получить заблокированные транзакции

    List<SuspiciousPhoneTransferDTO> findSuspiciousTransfers(); // Получить подозрительные транзакции
}
