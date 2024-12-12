package com.bank.account.mapper;

import com.bank.account.DTO.AccountDetailsDTO;
import com.bank.account.entity.AccountDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountDetailsMapper {

    @Mapping(target = "id", ignore = true)
    AccountDetails toEntitySave(AccountDetailsDTO dto);

    AccountDetails toEntity(AccountDetailsDTO dto);

    AccountDetailsDTO toDto(AccountDetails entity);

    @Mapping(target = "id", ignore = true)
    void toDtoUpdate(AccountDetailsDTO dto, @MappingTarget AccountDetails accountDetails);
}
