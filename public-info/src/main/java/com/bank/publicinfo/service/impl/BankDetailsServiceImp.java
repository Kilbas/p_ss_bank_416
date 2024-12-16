package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.service.interfaceEntity.BankDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BankDetailsServiceImp implements BankDetailsService {

    private String errorMessage;
    private final BankDetailsMapper mapper;
    private final BankDetailsRepository bankDetailsRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BankDetailsDTO> getAllBankDetails(Pageable pageable) {
        Page<BankDetails> all = bankDetailsRepository.findAll(pageable);
        return mapper.map(all.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public BankDetailsDTO getBankDetails(Long id) {
        BankDetails bankDetails = findBankDetailsById(id);
        return mapper.map(bankDetails);
    }

    @Override
    public void deleteBankDetail(Long id) {
        bankDetailsRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    public BankDetailsDTO addBankDetails(BankDetailsDTO bankDetailsCreateDTO) {
        BankDetails bankDetails = mapper.map(bankDetailsCreateDTO);
        return saveLicensesAndCertificates(bankDetailsCreateDTO, bankDetails);
    }

    @Override
    @AuditAnnotation
    public BankDetailsDTO updateBankDetails(Long id, BankDetailsDTO bankDetailsUpdateDTO) {
        BankDetails bankDetails = findBankDetailsById(id);
        mapper.updateBankDetailsFromDto(bankDetailsUpdateDTO, bankDetails);
        return saveLicensesAndCertificates(bankDetailsUpdateDTO, bankDetails);
    }

    private BankDetails findBankDetailsById(Long id) {
        return bankDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    errorMessage = String.format("Реквизиты банка не найдены для ID: %s", id);
                    log.error( errorMessage);
                    return new EntityNotFoundException( errorMessage);
                });
    }

    private BankDetailsDTO saveLicensesAndCertificates(BankDetailsDTO dto, BankDetails bankDetails) {
        saveLicenses(dto.getLicenses(), bankDetails);
        saveCertificates(dto.getCertificates(), bankDetails);
        return mapper.map(bankDetailsRepository.save(bankDetails));
    }

    private <T, U> void saveEntities(Set<T> entities, BankDetails bankDetails,
                                     Function<T, U> entityMapper,
                                     BiConsumer<BankDetails, Set<U>> setter) {
        if (!CollectionUtils.isEmpty(entities)) {
            Set<U> mappedEntities = entities.stream()
                    .map(entityMapper)
                    .collect(Collectors.toSet());
            setter.accept(bankDetails, mappedEntities);
        }
    }

    private void saveLicenses(Set<License> licenses, BankDetails bankDetails) {
        saveEntities(licenses, bankDetails, e -> {
            License license = new License();
            license.setPhoto(e.getPhoto());
            license.setBankDetailsLicense(bankDetails);
            return license;
        }, BankDetails::setLicenses);
    }

    private void saveCertificates(Set<Certificate> certificates, BankDetails bankDetails) {
        saveEntities(certificates, bankDetails, e -> {
            Certificate certificate = new Certificate();
            certificate.setPhoto(e.getPhoto());
            certificate.setBankDetailsCertificate(bankDetails);
            return certificate;
        }, BankDetails::setCertificates);
    }
}