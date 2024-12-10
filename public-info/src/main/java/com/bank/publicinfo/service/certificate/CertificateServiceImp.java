package com.bank.publicinfo.service.certificate;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.mapper.CertificateMapper;
import com.bank.publicinfo.repository.BankDetailsRepository;
import com.bank.publicinfo.repository.CertificateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
public class CertificateServiceImp implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final BankDetailsRepository bankDetailsRepository;
    private final CertificateMapper mapper;

    public CertificateServiceImp(CertificateRepository certificateRepository, BankDetailsRepository bankDetailsRepository, CertificateMapper mapper) {
        this.certificateRepository = certificateRepository;
        this.bankDetailsRepository = bankDetailsRepository;
        this.mapper = mapper;
    }

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
    @Transactional
    public  void deleteCertificate(Long id) {
        Certificate certificate = findCertificateById(id);
        BankDetails bankDetails = certificate.getBankDetailsCertificate();
        bankDetails.getCertificates().remove(certificate);
        certificateRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public CertificateDTO addCertificate(CertificateDTO certificateCreateDTO) {
        Certificate certificate = mapper.map(certificateCreateDTO);
        certificate.setBankDetailsCertificate(findBankDetailsById(certificateCreateDTO.getBankDetailsCertificate().getId()));
        Certificate save = certificateRepository.save(certificate);
        return mapper.map(save);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public CertificateDTO updateCertificate(Long id, CertificateDTO certificateCreateDTO) {
        Certificate certificate = findCertificateById(id);
        certificate.setBankDetailsCertificate(findBankDetailsById(certificateCreateDTO.getBankDetailsCertificate().getId()));
        mapper.updateCertificateFromDto(certificateCreateDTO, certificate);
        Certificate save = certificateRepository.save(certificate);
        return mapper.map(save);
    }

    private Certificate findCertificateById(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Сертификат не найден с id {}", id);
                    return new EntityNotFoundException("Сертификаты не найдена с id" + id);
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