package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SuspiciousPhoneTransfersMapper {
    SuspiciousPhoneTransferDTO toDTO(SuspiciousPhoneTransfers suspiciousPhoneTransfers);

    SuspiciousPhoneTransfers toEntity(SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO);

    @Mapping(target = "id", ignore = true)
        // Игнорируем обновление поля id
    void updateFromDto(SuspiciousPhoneTransferDTO dto, @MappingTarget SuspiciousPhoneTransfers suspiciousPhoneTransfers);


}
