package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;

import java.util.List;

public interface SuspiciousAccountTransfersService {

    SuspiciousAccountTransfersDTO createNewAccountTransfer(SuspiciousAccountTransfersDTO transferDTO);

    SuspiciousAccountTransfersDTO updateAccountTransfer(Long id, SuspiciousAccountTransfersDTO transferDTO);

    void deleteAccountTransfer(Long id);

    SuspiciousAccountTransfersDTO findByIdAccountTransfer(Long id);

    List<SuspiciousAccountTransfersDTO> findAllAccountTransfers();

    List<SuspiciousAccountTransfersDTO> findTransfersByReason(String reason); // Поиск по причине

    List<SuspiciousAccountTransfersDTO> findBlockedTransfers();// Получить заблокированные транзакции

    List<SuspiciousAccountTransfersDTO> findSuspiciousTransfers(); // Получить подозрительные транзакции


}

