package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;



@Mapper(componentModel = "spring")
public interface SuspiciousCardTransferMapper {

    SuspiciousCardTransferDTO toDTO(SuspiciousCardTransfer suspiciousCardTransfer);

    SuspiciousCardTransfer toEntity(SuspiciousCardTransferDTO suspiciousCardTransferDTO);

    @Mapping(target = "id", ignore = true) // Игнорируем обновление поля id
    void updateFromDto(SuspiciousCardTransferDTO transferDTO, @MappingTarget SuspiciousCardTransfer existing);

}









