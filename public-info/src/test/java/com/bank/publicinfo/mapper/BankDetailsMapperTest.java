package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.util.CreateBankDetailsAndLicenseAndCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Класс для тестирования BankDetailsMapper")
class BankDetailsMapperTest {
    private BankDetailsMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = Mappers.getMapper(BankDetailsMapper.class);
    }

    private void assertBankDetailsEquals(BankDetailsDTO bankDetailsDto, BankDetails bankDetails) {
        assertEquals(bankDetailsDto.getBik(), bankDetails.getBik());
        assertEquals(bankDetailsDto.getInn(), bankDetails.getInn());
        assertEquals(bankDetailsDto.getKpp(), bankDetails.getKpp());
        assertEquals(bankDetailsDto.getCorAccount(), bankDetails.getCorAccount());
        assertEquals(bankDetailsDto.getCity(), bankDetails.getCity());
        assertEquals(bankDetailsDto.getJointStockCompany(), bankDetails.getJointStockCompany());
        assertEquals(bankDetailsDto.getName(), bankDetails.getName());
        assertEquals(bankDetailsDto.getLicenses(), bankDetails.getLicenses());
        assertEquals(bankDetailsDto.getCertificates(), bankDetails.getCertificates());
    }

    private void assertBankDetailsDTOEquals(BankDetails bankDetails, BankDetailsDTO result) {
        assertEquals(bankDetails.getId(), result.getId());
        assertEquals(bankDetails.getBik(), result.getBik());
        assertEquals(bankDetails.getInn(), result.getInn());
        assertEquals(bankDetails.getKpp(), result.getKpp());
        assertEquals(bankDetails.getCorAccount(), result.getCorAccount());
        assertEquals(bankDetails.getCity(), result.getCity());
        assertEquals(bankDetails.getJointStockCompany(), result.getJointStockCompany());
        assertEquals(bankDetails.getName(), result.getName());
        assertEquals(bankDetails.getLicenses(), result.getLicenses());
        assertEquals(bankDetails.getCertificates(), result.getCertificates());
    }

    @Test
    @DisplayName("Маппинг из BankDetailsDTO в BankDetails")
    void mapBankDetailsDTOToBankDetails() {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        License license = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        bankDetailsDTO.getLicenses().add(license);
        bankDetailsDTO.getCertificates().add(certificate);

        BankDetails result = mapper.map(bankDetailsDTO);

        assertBankDetailsEquals(bankDetailsDTO, result);
        assertNull(result.getId());
    }

    @Test
    @DisplayName("Маппинг из BankDetails в BankDetailsDTO")
    void mapBankDetailsToBankDetailsDTO() {
        BankDetails bankDetails = CreateBankDetailsAndLicenseAndCertificate.createBankDetails();
        License license = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        bankDetails.getLicenses().add(license);
        bankDetails.getCertificates().add(certificate);

        BankDetailsDTO result = mapper.map(bankDetails);

        assertBankDetailsDTOEquals(bankDetails, result);
    }

    @Test
    @DisplayName("Маппинг из List<BankDetails> в List<BankDetailsDTO>")
    void mapAllBankDetailsToAllBankDetailsDTO() {
        BankDetails bankDetails = CreateBankDetailsAndLicenseAndCertificate.createBankDetails();
        List<BankDetails> allBankDetails = List.of(bankDetails);

        List<BankDetailsDTO> result = mapper.map(allBankDetails);

        assertEquals(allBankDetails.size(), result.size());
        assertBankDetailsDTOEquals(bankDetails, result.get(0));
    }

    @Test
    @DisplayName("Маппинг из BankDetailsDTO в BankDetails при обновлении сущности в БД")
    void updateBankDetailsFromDto() {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        BankDetails bankDetails = CreateBankDetailsAndLicenseAndCertificate.createBankDetails();
        License license = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        bankDetailsDTO.getLicenses().add(license);
        bankDetailsDTO.getCertificates().add(certificate);

        mapper.updateBankDetailsFromDto(bankDetailsDTO, bankDetails);

        assertBankDetailsEquals(bankDetailsDTO, bankDetails);
        assertNotNull(bankDetails.getId());
    }

    @Test
    @DisplayName("Маппинг из BankDetailsDTO в BankDetails без лицензий и сертификатов")
    void updateBankDetailsFromDtoWithoutLicAndCer() {
        BankDetailsDTO bankDetailsDTO = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        BankDetails bankDetails = CreateBankDetailsAndLicenseAndCertificate.createBankDetails();
        bankDetails.setLicenses(null);
        bankDetails.setCertificates(null);

        mapper.updateBankDetailsFromDto(bankDetailsDTO, bankDetails);

        assertNotNull(bankDetails.getId());
        assertTrue(bankDetails.getLicenses().isEmpty());
        assertTrue(bankDetails.getCertificates().isEmpty());
    }
}