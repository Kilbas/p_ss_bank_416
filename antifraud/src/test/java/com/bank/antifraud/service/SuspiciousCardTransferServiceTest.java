package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SuspiciousCardTransferServiceTest {

    @Mock
    private SuspiciousAccountTransfersRepository repository;
    @Mock
    private SuspiciousAccountTransfersMapper mapper;

    @InjectMocks
    private SuspiciousCardTransferServiceTest suspiciousCardTransferServiceTest;

    private SuspiciousCardTransferDTO dto;
    private SuspiciousCardTransfer entity;


    @BeforeEach
    void setUp() {
        dto = new SuspiciousCardTransferDTO();
        dto.setCardTransferId(1L);
        dto.setBlocked(true);
        dto.setSuspicious(false);
        dto.setBlockedReason("reason");
        dto.setSuspiciousReason("reason reason");


        entity = new SuspiciousCardTransfer();


    }

    @Test
    void findByIdCardTransfer() {
    }

    @Test
    void findAllCardTransfers() {
    }

    @Test
    void createNewCardTransfer() {
    }

    @Test
    void updateCardTransfer() {
    }

    @Test
    void deleteCardTransfer() {
    }

    @Test
    void findTransfersByReason() {
    }

    @Test
    void findBlockedTransfers() {
    }

    @Test
    void findSuspiciousTransfers() {
    }
}