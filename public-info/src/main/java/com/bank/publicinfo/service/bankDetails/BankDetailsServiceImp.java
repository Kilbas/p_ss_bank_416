package com.bank.publicinfo.service.bankDetails;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.entity.License;
import com.bank.publicinfo.mapper.BankDetailsMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BankDetailsServiceImp implements BankDetailsService {

    private final BankDetailsMapper mapper;
    private final BankDetailsRepository bankDetailsRepository;

    public BankDetailsServiceImp(BankDetailsMapper mapper, BankDetailsRepository bankDetailsRepository) {
        this.mapper = mapper;
        this.bankDetailsRepository = bankDetailsRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BankDetailsDTO> getAllBankDetails(Pageable pageable) {
        log.info("Запрос на получение всех реквизитов банка");
        Page<BankDetails> all = bankDetailsRepository.findAll(pageable);
        log.info("Получено  реквизита(ов) банка");
        return mapper.map(all.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public BankDetailsDTO getBankDetails(Long id) {
        log.info("Запрос на получение реквизитов банка с ID: {}", id);
        BankDetails bankDetails = findBankDetailsById(id);
        BankDetailsDTO map = mapper.map(bankDetails);
        log.info("Получены реквизиты банка: {}", bankDetails);
        return map;
    }

    @Override
    @Transactional
    @AuditAnnotation
    public void deleteBankDetail(Long id) {
        bankDetailsRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public BankDetailsDTO addBankDetail(BankDetailsDTO bankDetailsCreateDTO) {
        BankDetails bankDetails = mapper.map(bankDetailsCreateDTO);
        return saveLicensesAndCertificates(bankDetailsCreateDTO, bankDetails);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public BankDetailsDTO updateBankDetail(Long id, BankDetailsDTO bankDetailsUpdateDTO) {
        BankDetails bankDetails = findBankDetailsById(id);
        mapper.updateBankDetailsFromDto(bankDetailsUpdateDTO, bankDetails);
        return saveLicensesAndCertificates(bankDetailsUpdateDTO, bankDetails);
    }

    private BankDetails findBankDetailsById(Long id) {
        return bankDetailsRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Реквизиты банка не найдены для ID: {}", id);
                    return new EntityNotFoundException("Реквизиты банка не найдены");
                });
    }

    private BankDetailsDTO saveLicensesAndCertificates(BankDetailsDTO dto, BankDetails bankDetails) {
        saveLicenses(dto.getLicenses(), bankDetails);
        saveCertificates(dto.getCertificates(), bankDetails);
        return mapper.map(bankDetailsRepository.save(bankDetails));
    }

    private void saveLicenses(Set<License> licenses, BankDetails bankDetails) {
        if (!CollectionUtils.isEmpty(licenses)) {
            bankDetails.setLicenses(licenses.stream()
                    .map(e -> {
                        License license = new License();
                        license.setPhoto(e.getPhoto());
                        license.setBankDetailsLicense(bankDetails);
                        return license;
                    })
                    .collect(Collectors.toSet()));
        }
    }

    private void saveCertificates(Set<Certificate> certificates, BankDetails bankDetails) {
        if (!CollectionUtils.isEmpty(certificates)) {
            bankDetails.setCertificates(certificates.stream()
                    .map(e -> {
                        Certificate certificate = new Certificate();
                        certificate.setPhoto(e.getPhoto());
                        certificate.setBankDetailsCertificate(bankDetails);
                        return certificate;
                    })
                    .collect(Collectors.toSet()));
        }
    }
}