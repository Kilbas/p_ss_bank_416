package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;

import java.util.List;

public interface SuspiciousPhoneTransfersService {
    SuspiciousPhoneTransferDTO findById(Long id);

    List<SuspiciousPhoneTransferDTO> findAll();

    SuspiciousPhoneTransferDTO create(SuspiciousPhoneTransferDTO dto);

    SuspiciousPhoneTransferDTO update(Long id, SuspiciousPhoneTransferDTO dto);

    void delete(Long id);

    List<SuspiciousPhoneTransferDTO> findTransfersByReason(String reason); // Поиск по причине
}
