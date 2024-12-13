package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.LicenseRepository;
import com.bank.publicinfo.service.interfaceEntity.LicenseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LicenseServiceImp implements LicenseService {

    private String errorMessage;
    private final LicenseRepository licenseRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final LicenseMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<LicenseDTO> getAllLicenses(Pageable pageable) {
        Page<License> all = licenseRepository.findAll(pageable);
        return mapper.map(all.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public LicenseDTO getLicense(Long id) {
        License license = findLicenseById(id);
        return mapper.map(license);
    }

    @Override
    @AuditAnnotation
    public void deleteLicense(Long id) {
        License license = findLicenseById(id);
        BankDetails bankDetails = license.getBankDetailsLicense();
        bankDetails.getLicenses().remove(license);
        licenseRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    public LicenseDTO addLicense(LicenseDTO licenseCreateDTO) {
        License license = mapper.map(licenseCreateDTO);
        license.setBankDetailsLicense(findBankDetailsById(licenseCreateDTO.getBankDetailsLicense().getId()));
        return mapper.map(licenseRepository.save(license));
    }

    @Override
    @AuditAnnotation
    public LicenseDTO updateLicense(Long id, LicenseDTO licenseCreateDTO) {
        License license = findLicenseById(id);
        license.setBankDetailsLicense(findBankDetailsById(licenseCreateDTO.getBankDetailsLicense().getId()));
        mapper.updateLicenseFromDto(licenseCreateDTO, license);
        return mapper.map(licenseRepository.save(license));
    }

    private <T> T findEntityById(Long id, Function<Long, Optional<T>> findFunction, String entityName) {
        return findFunction.apply(id)
                .orElseThrow(() -> {
                    errorMessage = String.format("%s не найден с id %s", entityName, id);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    private License findLicenseById(Long id) {
        return findEntityById(id, licenseRepository::findById, "Лицензия");
    }

    private BankDetails findBankDetailsById(Long id) {
        return findEntityById(id, bankDetailsRepository::findById, "Реквизиты банка");
    }
}