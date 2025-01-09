package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.entity.Certificate;
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


@DisplayName("Класс для тестирования CertificateMapper")
class CertificateMapperTest {
    private CertificateMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = Mappers.getMapper(CertificateMapper.class);
    }

    private void assertCertificateEquals(CertificateDTO certificateDto, Certificate certificate) {
        assertArrayEquals(certificateDto.getPhoto(), certificate.getPhoto());
    }

    private void assertCertificateDTOEquals(Certificate certificate, CertificateDTO result) {
        assertEquals(certificate.getId(), result.getId());
        assertArrayEquals(certificate.getPhoto(), result.getPhoto());
        assertEquals(certificate.getBankDetailsCertificate(), result.getBankDetailsCertificate());
    }

    @Test
    @DisplayName("Маппинг из CertificateDTO в Certificate")
    void mapCertificateDTOToCertificate() {
        CertificateDTO certificateDTO = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();

        Certificate result = mapper.map(certificateDTO);

        assertCertificateEquals(certificateDTO, result);
        assertNull(result.getId());
    }

    @Test
    @DisplayName("Маппинг из Certificate в CertificateDTO")
    void mapCertificateToCertificateDTO() {
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();

        CertificateDTO result = mapper.map(certificate);

        assertCertificateDTOEquals(certificate, result);
    }

    @Test
    @DisplayName("Маппинг из List<Certificate> в List<CertificateDTO>")
    void mapAllCertificateToAllCertificateDTO() {
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        List<Certificate> allCertificate = List.of(certificate);

        List<CertificateDTO> result = mapper.map(allCertificate);

        assertEquals(allCertificate.size(), result.size());
        assertCertificateDTOEquals(certificate, result.get(0));
    }

    @Test
    @DisplayName("Маппинг из CertificateDTO в Certificate при обновлении сущности в БД")
    void updateCertificateFromDto() {
        CertificateDTO certificateDTO = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();

        mapper.updateCertificateFromDto(certificateDTO, certificate);

        assertCertificateEquals(certificateDTO, certificate);
        assertNotNull(certificate.getId());
    }
}
