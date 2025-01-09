package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SuspiciousPhoneTransfersMapper {
    SuspiciousPhoneTransferDTO toDTO(SuspiciousPhoneTransfers suspiciousPhoneTransfers);

    SuspiciousPhoneTransfers toEntity(SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO);

    @Mapping(target = "id", ignore = true)
    void updateFromDto(SuspiciousPhoneTransferDTO dto, @MappingTarget SuspiciousPhoneTransfers suspiciousPhoneTransfers);


}
