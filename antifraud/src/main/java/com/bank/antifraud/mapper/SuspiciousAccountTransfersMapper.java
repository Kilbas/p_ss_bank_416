package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SuspiciousAccountTransfersMapper {
    SuspiciousAccountTransfersDTO toDTO(SuspiciousAccountTransfers suspiciousAccountTransfers);

    SuspiciousAccountTransfers toEntity(SuspiciousAccountTransfersDTO suspiciousAccountTransfersDTO);


}
