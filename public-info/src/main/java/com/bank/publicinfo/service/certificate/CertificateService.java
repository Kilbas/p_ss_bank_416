package com.bank.publicinfo.service.certificate;

import com.bank.publicinfo.dto.CertificateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CertificateService {
    List<CertificateDTO> getAllCertificates(Pageable pageable);

    CertificateDTO getCertificate(Long id);

    CertificateDTO addCertificate(CertificateDTO certificate);

    CertificateDTO updateCertificate(Long id, CertificateDTO updateCertificate);

    void deleteCertificate(Long id);
}