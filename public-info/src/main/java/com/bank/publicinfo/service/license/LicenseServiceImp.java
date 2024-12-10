package com.bank.publicinfo.service.license;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.LicenseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
public class LicenseServiceImp implements LicenseService {

    private final LicenseRepository licenseRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final LicenseMapper mapper;

    public LicenseServiceImp(LicenseRepository licenseRepository,
                             BankDetailsRepository bankDetailsRepository, LicenseMapper mapper) {
        this.licenseRepository = licenseRepository;
        this.bankDetailsRepository = bankDetailsRepository;
        this.mapper = mapper;
    }

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
    @Transactional
    public void deleteLicense(Long id) {
        License license = findLicenseById(id);
        BankDetails bankDetails = license.getBankDetailsLicense();
        bankDetails.getLicenses().remove(license);
        licenseRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public LicenseDTO addLicense(LicenseDTO licenseCreateDTO) {
        License license = mapper.map(licenseCreateDTO);
        license.setBankDetailsLicense(findBankDetailsById(licenseCreateDTO.getBankDetailsLicense().getId()));
        License save = licenseRepository.save(license);
        return mapper.map(save);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public LicenseDTO updateLicense(Long id, LicenseDTO licenseCreateDTO) {
        License license = findLicenseById(id);
        license.setBankDetailsLicense(findBankDetailsById(licenseCreateDTO.getBankDetailsLicense().getId()));
        mapper.updateLicenseFromDto(licenseCreateDTO, license);
        License save = licenseRepository.save(license);
        return mapper.map(save);
    }

    private License findLicenseById(Long id) {
        return licenseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Лицензия не найдена с id {}", id);
                    return new EntityNotFoundException("Лицензия не найдена с id" + id);
                });
    }

    private BankDetails findBankDetailsById(Long id) {
        return bankDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Реквизиты банка не найдены с id {}", id);
                    return new EntityNotFoundException("Реквизиты банка не найдены с id" + id);
                });
    }
}