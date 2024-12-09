package com.bank.publicinfo.service.certificate;

import com.bank.publicinfo.dto.CertificateDTO;

import java.util.List;

public interface CertificateService {
    List<CertificateDTO> getAllCertificates();

    CertificateDTO getCertificate(Long id);

    CertificateDTO addCertificate(CertificateDTO certificate);

    CertificateDTO updateCertificate(Long id, CertificateDTO updateCertificate);

    CertificateDTO deleteCertificate(Long id);
}
