package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.LicenseRepository;
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
@DisplayName("Класс для тестирование LicenseService.")
class LicenseServiceImpTest {

    private static final Long ID = 11L;

    @Mock
    private LicenseRepository repository;

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    private LicenseMapper mapper;

    private LicenseServiceImp serviceImp;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(LicenseMapper.class);
        serviceImp = new LicenseServiceImp(repository, bankDetailsRepository, mapper);
    }

    @Test
    @DisplayName("Тест на получение списка License.")
    void getAllLicense() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<License> page = new PageImpl<>(List.of(CreateBankDetailsAndLicenseAndCertificate.createLicense(),
                CreateBankDetailsAndLicenseAndCertificate.createLicense()));
        doReturn(page).when(repository).findAll(pageable);

        List<LicenseDTO> result = serviceImp.getAllLicenses(pageable);

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(pageable.getPageSize(), result.size());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Тест на получение License.")
    void getLicense() {
        License expect = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        doReturn(Optional.of(expect)).when(repository).findById(expect.getId());

        LicenseDTO result = serviceImp.getLicense(expect.getId());

        assertNotNull(result);
        assertEquals(expect.getId(), result.getId());
        verify(repository).findById(expect.getId());
    }

    @Test
    @DisplayName("Тест на получение License. Не удалось найти сущность.")
    void getLicenseWhenEntityNotFound() {
        doReturn(Optional.empty()).when(repository).findById(ID);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> serviceImp.getLicense(ID));

        assertEquals(String.format("Лицензия не найден с id %s", ID), exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    @DisplayName("Тест на удаление License.")
    void deleteCertificate() {
        License expect = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        doReturn(Optional.of(expect)).when(repository).findById(expect.getId());
        doNothing().when(repository).deleteById(expect.getId());

        serviceImp.deleteLicense(expect.getId());

        verify(repository).deleteById(expect.getId());
        verify(repository).findById(expect.getId());
    }

    @Test
    @DisplayName("Тест на добавление License.")
    void addLicense() {
        LicenseDTO expect = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        BankDetails bankDetails = expect.getBankDetailsLicense();
        License map = mapper.map(expect);
        map.setId(expect.getId());
        doReturn(Optional.of(bankDetails)).when(bankDetailsRepository).findById(bankDetails.getId());
        doReturn(map).when(repository).save(any());

        LicenseDTO result = serviceImp.addLicense(expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertArrayEquals(expect.getPhoto(), result.getPhoto());
        verify(repository).save(any());
        verify(bankDetailsRepository).findById(bankDetails.getId());
    }

    @Test
    @DisplayName("Тест на добавление License. Не найден BankDetails")
    void addLicenseWhenEntityNotFound() {
        LicenseDTO expect = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        BankDetails bankDetails = expect.getBankDetailsLicense();
        doReturn(Optional.empty()).when(bankDetailsRepository).findById(bankDetails.getId());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> serviceImp.addLicense(expect));

        assertEquals(String.format("Реквизиты банка не найден с id %s", bankDetails.getId()), entityNotFoundException.getMessage());
        verify(bankDetailsRepository).findById(bankDetails.getId());
    }

    @Test
    @DisplayName("Тест на изменение License.")
    void updateLicense() {
        LicenseDTO expect = CreateBankDetailsAndLicenseAndCertificate.createLicenseDTO();
        BankDetails bankDetails = expect.getBankDetailsLicense();
        License license = CreateBankDetailsAndLicenseAndCertificate.createLicense();
        mapper.updateLicenseFromDto(expect, license);
        doReturn(Optional.of(bankDetails)).when(bankDetailsRepository).findById(bankDetails.getId());
        doReturn(Optional.of(license)).when(repository).findById(expect.getId());
        doReturn(license).when(repository).save(any());

        LicenseDTO result = serviceImp.updateLicense(expect.getId(), expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertArrayEquals(expect.getPhoto(), result.getPhoto());
        verify(repository).save(any());
        verify(repository).findById(expect.getId());
        verify(bankDetailsRepository).findById(bankDetails.getId());
    }
}