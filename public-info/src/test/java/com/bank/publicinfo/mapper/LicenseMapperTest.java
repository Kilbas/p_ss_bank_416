package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.util.CreateBankDetailsAndLicenseAndCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Класс для тестирования LicenseMapper")
class LicenseMapperTest {
    private LicenseMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = Mappers.getMapper(LicenseMapper.class);
    }

    private void assertLicenseEquals(LicenseDTO licenseDto, License license) {
        assertArrayEquals(licenseDto.getPhoto(), license.getPhoto());
    }

    private void assertLicenseDTOEquals(License license, LicenseDTO result) {
        assertEquals(license.getId(), result.getId());
        assertArrayEquals(license.getPhoto(), result.getPhoto());
        assertEquals(license.getBankDetailsLicense(), result.getBankDetailsLicense());
    }

    @Test
    @DisplayName("Маппинг из LicenseDTO в License")
    void mapLicenseDTOToLicense() {
        LicenseDTO licenseDTO = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        License result = mapper.map(licenseDTO);
        assertLicenseEquals(licenseDTO, result);
        assertNull(result.getId());
    }

    @Test
    @DisplayName("Маппинг из License в LicenseDTO")
    void mapLicenseToLicenseDTO() {
        License license = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        LicenseDTO result = mapper.map(license);
        assertLicenseDTOEquals(license, result);
    }

    @Test
    @DisplayName("Маппинг из List<License> в List<LicenseDTO>")
    void mapAllLicenseToAllLicenseDTO() {
        License license = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        List<License> allLicense = List.of(license);
        List<LicenseDTO> result = mapper.map(allLicense);
        assertEquals(allLicense.size(), result.size());
        assertLicenseDTOEquals(license, result.get(0));
    }

    @Test
    @DisplayName("Маппинг из LicenseDTO в License при обновлении сущности в БД")
    void updateLicenseFromDto() {
        LicenseDTO licenseDTO = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        License certificate = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        mapper.updateLicenseFromDto(licenseDTO, certificate);
        assertLicenseEquals(licenseDTO, certificate);
        assertNotNull(certificate.getId());
    }
}
