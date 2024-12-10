package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SuspiciousAccountTransfersMapper {
    SuspiciousAccountTransfersDTO toDTO(SuspiciousAccountTransfers suspiciousAccountTransfers);

    SuspiciousAccountTransfers toEntity(SuspiciousAccountTransfersDTO suspiciousAccountTransfersDTO);

    @Mapping(target = "id", ignore = true) // Игнорируем обновление поля id
    void updateFromDto(SuspiciousAccountTransfersDTO transferDTO, @MappingTarget SuspiciousAccountTransfers existing);
}
