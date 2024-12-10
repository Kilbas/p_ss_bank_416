package com.bank.publicinfo.service.license;

import com.bank.publicinfo.dto.LicenseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LicenseService {
    List<LicenseDTO> getAllLicenses(Pageable pageable);

    LicenseDTO getLicense(Long id);

    LicenseDTO addLicense(LicenseDTO licence);

    LicenseDTO updateLicense(Long id, LicenseDTO license);

    void deleteLicense(Long id);
}