package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SuspiciousCardTransferMapper {
    SuspiciousCardTransferDTO toDTO(SuspiciousCardTransfer suspiciousCardTransfer);

    SuspiciousCardTransfer toEntity(SuspiciousCardTransferDTO suspiciousCardTransferDTO);

    void toDtoUpdate(SuspiciousCardTransferDTO dto, @MappingTarget SuspiciousCardTransfer suspiciousCardTransfer);


}
