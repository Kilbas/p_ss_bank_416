package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.entity.Atm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AtmMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    Atm map(AtmDTO atmCreateDTO);

    AtmDTO map(Atm atm);

    List<AtmDTO> map(List<Atm> allAtm);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "address", source = "address",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "startOfWork", source = "startOfWork",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "endOfWork", source = "endOfWork",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "allHours", source = "allHours",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "branch", source = "branch",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAtmFromDto(AtmDTO dto, @MappingTarget Atm atm);
}