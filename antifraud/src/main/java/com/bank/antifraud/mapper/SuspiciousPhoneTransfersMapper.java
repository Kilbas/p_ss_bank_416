package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SuspiciousPhoneTransfersMapper {
    SuspiciousPhoneTransferDTO toDTO(SuspiciousPhoneTransfers suspiciousPhoneTransfers);

    SuspiciousPhoneTransfers toEntity(SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO);

    void toDtoUpdate(SuspiciousPhoneTransferDTO dto, @MappingTarget SuspiciousPhoneTransfers suspiciousPhoneTransfers);



}
