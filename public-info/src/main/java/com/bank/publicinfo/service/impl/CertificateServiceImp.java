package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.CertificateRepository;
import com.bank.publicinfo.service.interfaceEntity.CertificateService;
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
public class CertificateServiceImp implements CertificateService {

    private String errorMessage;
    private final CertificateRepository certificateRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final CertificateMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CertificateDTO> getAllCertificates(Pageable pageable) {
        Page<Certificate> all = certificateRepository.findAll(pageable);
        return mapper.map(all.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateDTO getCertificate(Long id) {
        Certificate certificate = findCertificateById(id);
        return mapper.map(certificate);
    }

    @Override
    public  void deleteCertificate(Long id) {
        Certificate certificate = findCertificateById(id);
        BankDetails bankDetails = certificate.getBankDetailsCertificate();
        bankDetails.getCertificates().remove(certificate);
        certificateRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    public CertificateDTO addCertificate(CertificateDTO certificateCreateDTO) {
        Certificate certificate = mapper.map(certificateCreateDTO);
        certificate.setBankDetailsCertificate(findBankDetailsById(certificateCreateDTO.getBankDetailsCertificate().getId()));
        return mapper.map(certificateRepository.save(certificate));
    }

    @Override
    @AuditAnnotation
    public CertificateDTO updateCertificate(Long id, CertificateDTO certificateCreateDTO) {
        Certificate certificate = findCertificateById(id);
        certificate.setBankDetailsCertificate(findBankDetailsById(certificateCreateDTO.getBankDetailsCertificate().getId()));
        mapper.updateCertificateFromDto(certificateCreateDTO, certificate);
        return mapper.map(certificateRepository.save(certificate));
    }

    private <T> T findEntityById(Long id, Function<Long, Optional<T>> findFunction, String entityName) {
        return findFunction.apply(id)
                .orElseThrow(() -> {
                    errorMessage = String.format("%s не найден с id %s", entityName, id);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    private Certificate findCertificateById(Long id) {
        return findEntityById(id, certificateRepository::findById, "Сертификат");
    }

    private BankDetails findBankDetailsById(Long id) {
        return findEntityById(id, bankDetailsRepository::findById, "Реквизиты банка");
    }
}