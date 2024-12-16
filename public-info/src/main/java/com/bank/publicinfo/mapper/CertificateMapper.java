package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.entity.Certificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CertificateMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "bankDetailsCertificate", ignore = true)
    Certificate map(CertificateDTO certificateCreateDTO);

    CertificateDTO map(Certificate certificate);

    List<CertificateDTO> map(List<Certificate> allCertificate);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "photo", source = "photo",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "bankDetailsCertificate", ignore = true)
    void updateCertificateFromDto(CertificateDTO dto, @MappingTarget Certificate certificate);
}