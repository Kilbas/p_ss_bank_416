package com.bank.antifraud.serviceImpl;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class SuspiciousAccountTransfersServiceImplTest {
    @Mock
    private  SuspiciousAccountTransfersRepository repository;
    @Mock
    private  SuspiciousAccountTransfersMapper mapper;

    @InjectMocks
    private SuspiciousAccountTransfersServiceImpl service;

    private SuspiciousAccountTransfersDTO dto;
    private SuspiciousAccountTransfers entity;


    @BeforeEach
    void setUp() {
        entity = new SuspiciousAccountTransfers();
        entity.setId(10L);
        entity.setAccountTransferId(777L);
        entity.setSuspiciousReason("test");
        entity.setBlocked(false);
        entity.setSuspicious(true);


        dto = new SuspiciousAccountTransfersDTO();
        dto.setId(10L);
        dto.setAccountTransferId(777L);
        dto.setSuspiciousReason("test");
        dto.setBlocked(false);
        dto.setSuspicious(true);


    }


    @Test
    @DisplayName("createNewAccountTransfer: сохраняет и возвращает DTO")
    void createNewAccountTransfer_ok() {
        given(mapper.toEntity(dto)).willReturn(entity);

        given(repository.save(entity)).willReturn(entity);

        given(mapper.toDTO(entity)).willReturn(dto);



        var result = service.createNewAccountTransfer(dto);


        assertEquals(dto, result);
        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper).toDTO(entity);
        verifyNoMoreInteractions(repository, mapper);
    }

    @Test
    void updateAccountTransfer() {
    }

    @Test
    void deleteAccountTransfer() {
    }

    @Test
    void findByIdAccountTransfer() {
    }

    @Test
    void findAllAccountTransfers() {
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