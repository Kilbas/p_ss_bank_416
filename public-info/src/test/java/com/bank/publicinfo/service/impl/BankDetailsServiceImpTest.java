package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.util.CreateBankDetailsAndLicenseAndCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
@DisplayName("Класс для тестирование BankDetailsService.")
class BankDetailsServiceImpTest {

    private static final Long ID = 11L;

    @Mock
    private BankDetailsRepository repository;

    private BankDetailsMapper mapper;

    private BankDetailsServiceImp serviceImp;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(BankDetailsMapper.class);
        serviceImp = new BankDetailsServiceImp(mapper, repository);
    }

    @Test
    @DisplayName("Тест на получение списка BankDetails.")
    void getAllBankDetails() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<BankDetails> page = new PageImpl<>(List.of(CreateBankDetailsAndLicenseAndCertificate.createBankDetails(),
                CreateBankDetailsAndLicenseAndCertificate.createBankDetails()));
        doReturn(page).when(repository).findAll(pageable);

        List<BankDetailsDTO> result = serviceImp.getAllBankDetails(pageable);

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(pageable.getPageSize(), result.size());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Тест на получение BankDetails.")
    void getBankDetails() {
        BankDetails expect = CreateBankDetailsAndLicenseAndCertificate.createBankDetails();
        doReturn(Optional.of(expect)).when(repository).findById(expect.getId());

        BankDetailsDTO result = serviceImp.getBankDetails(expect.getId());

        assertNotNull(result);
        assertEquals(expect.getId(), result.getId());
        verify(repository).findById(expect.getId());
    }

    @Test
    @DisplayName("Тест на получение BankDetails. Не удалось найти сущность.")
    void getBankDetailsWhenEntityNotFound() {
        doReturn(Optional.empty()).when(repository).findById(ID);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> serviceImp.getBankDetails(ID));

        assertEquals(String.format("Реквизиты банка не найдены для ID: %s", ID), exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    @DisplayName("Тест на удаление BankDetails.")
    void deleteBankDetail() {
        BankDetails expect = CreateBankDetailsAndLicenseAndCertificate.createBankDetails();
        doNothing().when(repository).deleteById(expect.getId());

        serviceImp.deleteBankDetail(expect.getId());

        verify(repository).deleteById(expect.getId());
    }

    @Test
    @DisplayName("Тест на добавление BankDetails.")
    void addBankDetails() {
        BankDetailsDTO expect = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        License license = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        expect.getLicenses().add(license);
        expect.getCertificates().add(certificate);
        BankDetails map = mapper.map(expect);
        map.setId(expect.getId());
        doReturn(map).when(repository).save(any());

        BankDetailsDTO result = serviceImp.addBankDetails(expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(expect.getBik(), result.getBik());
        assertEquals(expect.getKpp(), result.getKpp());
        assertEquals(expect.getLicenses(), result.getLicenses());
        assertEquals(expect.getCertificates(), result.getCertificates());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Тест на изменение BankDetails.")
    void updateBankDetails() {
        BankDetailsDTO expect = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        BankDetails bankDetails = CreateBankDetailsAndLicenseAndCertificate.createBankDetails();
        mapper.updateBankDetailsFromDto(expect, bankDetails);
        doReturn(Optional.of(bankDetails)).when(repository).findById(expect.getId());
        doReturn(bankDetails).when(repository).save(any());

        BankDetailsDTO result = serviceImp.updateBankDetails(expect.getId(), expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(expect.getCorAccount(), result.getCorAccount());
        assertEquals(expect.getInn(), result.getInn());
        assertEquals(expect.getCity(), result.getCity());
        assertEquals(expect.getJointStockCompany(), result.getJointStockCompany());
        assertEquals(expect.getName(), result.getName());
        verify(repository).save(any());
        verify(repository).findById(expect.getId());
    }
}