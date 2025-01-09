package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.CertificateRepository;
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
@DisplayName("Класс для тестирование CertificateService.")
class CertificateServiceImpTest {

    private static final Long ID = 11L;

    @Mock
    private CertificateRepository repository;

    @Mock
    private BankDetailsRepository bankDetailsRepository;

    private CertificateMapper mapper;

    private CertificateServiceImp serviceImp;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CertificateMapper.class);
        serviceImp = new CertificateServiceImp(repository, bankDetailsRepository, mapper);
    }

    @Test
    @DisplayName("Тест на получение списка Certificate.")
    void getAllCertificate() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<Certificate> page = new PageImpl<>(List.of(CreateBankDetailsAndLicenseAndCertificate.createCertificate(),
                CreateBankDetailsAndLicenseAndCertificate.createCertificate()));
        doReturn(page).when(repository).findAll(pageable);

        List<CertificateDTO> result = serviceImp.getAllCertificates(pageable);

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(pageable.getPageSize(), result.size());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Тест на получение Certificate.")
    void getCertificate() {
        Certificate expect = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        doReturn(Optional.of(expect)).when(repository).findById(expect.getId());

        CertificateDTO result = serviceImp.getCertificate(expect.getId());

        assertNotNull(result);
        assertEquals(expect.getId(), result.getId());
        verify(repository).findById(expect.getId());
    }

    @Test
    @DisplayName("Тест на получение Certificate. Не удалось найти сущность.")
    void getCertificateWhenEntityNotFound() {
        doReturn(Optional.empty()).when(repository).findById(ID);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> serviceImp.getCertificate(ID));

        assertEquals(String.format("Сертификат не найден с id %s", ID), exception.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    @DisplayName("Тест на удаление Certificate.")
    void deleteCertificate() {
        Certificate expect = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        doReturn(Optional.of(expect)).when(repository).findById(expect.getId());
        doNothing().when(repository).deleteById(expect.getId());

        serviceImp.deleteCertificate(expect.getId());

        verify(repository).deleteById(expect.getId());
        verify(repository).findById(expect.getId());
    }

    @Test
    @DisplayName("Тест на добавление Certificate.")
    void addCertificate() {
        CertificateDTO expect = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        BankDetails bankDetails = expect.getBankDetailsCertificate();
        Certificate map = mapper.map(expect);
        map.setId(expect.getId());
        doReturn(Optional.of(bankDetails)).when(bankDetailsRepository).findById(bankDetails.getId());
        doReturn(map).when(repository).save(any());

        CertificateDTO result = serviceImp.addCertificate(expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertArrayEquals(expect.getPhoto(), result.getPhoto());
        verify(repository).save(any());
        verify(bankDetailsRepository).findById(bankDetails.getId());
    }

    @Test
    @DisplayName("Тест на добавление Certificate. Не найден BankDetails")
    void addCertificateWhenEntityNotFound() {
        CertificateDTO expect = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        BankDetails bankDetails = expect.getBankDetailsCertificate();
        doReturn(Optional.empty()).when(bankDetailsRepository).findById(bankDetails.getId());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> serviceImp.addCertificate(expect));

        assertEquals(String.format("Реквизиты банка не найден с id %s", bankDetails.getId()), entityNotFoundException.getMessage());
        verify(bankDetailsRepository).findById(bankDetails.getId());
    }

    @Test
    @DisplayName("Тест на изменение Certificate.")
    void updateCertificate() {
        CertificateDTO expect = CreateBankDetailsAndLicenseAndCertificate.createCertificateDTO();
        BankDetails bankDetails = expect.getBankDetailsCertificate();
        Certificate certificate = CreateBankDetailsAndLicenseAndCertificate.createCertificate();
        mapper.updateCertificateFromDto(expect, certificate);
        doReturn(Optional.of(bankDetails)).when(bankDetailsRepository).findById(bankDetails.getId());
        doReturn(Optional.of(certificate)).when(repository).findById(expect.getId());
        doReturn(certificate).when(repository).save(any());

        CertificateDTO result = serviceImp.updateCertificate(expect.getId(), expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertArrayEquals(expect.getPhoto(), result.getPhoto());
        verify(repository).save(any());
        verify(repository).findById(expect.getId());
        verify(bankDetailsRepository).findById(bankDetails.getId());
    }
}
