package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.dto.SuspiciousCardTransferDTO;

import java.util.List;

public interface SuspiciousAccountTransfersService {

    SuspiciousAccountTransfersDTO create(SuspiciousAccountTransfersDTO transferDTO);

    SuspiciousAccountTransfersDTO update(Long id, SuspiciousAccountTransfersDTO transferDTO);

    void delete(Long id);

    SuspiciousAccountTransfersDTO findById(Long id);

    List<SuspiciousAccountTransfersDTO> findAll();
}
