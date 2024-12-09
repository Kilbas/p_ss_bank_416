package com.bank.publicinfo.mapper;


import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.entity.License;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LicenseMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "bankDetailsLicense", ignore = true)
    License map(LicenseDTO licenseCreateDTO);

    LicenseDTO map(License license);

    List<LicenseDTO> map(List<License> allLicense);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "photo", source = "photo",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "bankDetailsLicense", ignore = true)
    void updateLicenseFromDto(LicenseDTO dto, @MappingTarget License license);
}
