package com.bank.publicinfo.service.license;


import com.bank.publicinfo.dto.LicenseDTO;

import java.util.List;

public interface LicenseService {
    List<LicenseDTO> getAllLicenses();

    LicenseDTO getLicense(Long id);

    LicenseDTO addLicense(LicenseDTO licence);

    LicenseDTO updateLicense(Long id, LicenseDTO license);

    LicenseDTO deleteLicense(Long id);
}
