package com.bank.publicinfo.util;

import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.dto.CertificateDTO;
import com.bank.publicinfo.dto.LicenseDTO;
import com.bank.publicinfo.entity.BankDetails;
import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.entity.License;
import org.apache.commons.lang.RandomStringUtils;
import org.aspectj.runtime.internal.Conversions;

import java.util.Base64;
import java.util.HashSet;
import java.util.Random;

public class CreateBankDetailsAndLicenseAndCertificate {
    private static final BankDetails BANK_DETAILS = createBankDetails();
    private static final byte[] PHOTO = Base64.getDecoder().decode("3VUd9zmdTgS1+BOamSoYSg==");

    public static BankDetailsDTO createBankDetailsDTO() {
        BankDetailsDTO bankDetailsDTO = new BankDetailsDTO();
        bankDetailsDTO.setId(createRandomLong());
        bankDetailsDTO.setBik(createRandomLong());
        bankDetailsDTO.setInn(createRandomLong());
        bankDetailsDTO.setKpp(createRandomLong());
        bankDetailsDTO.setCorAccount(Conversions.intValue(createRandomLong()));
        bankDetailsDTO.setCity(createRandomString());
        bankDetailsDTO.setJointStockCompany(createRandomString());
        bankDetailsDTO.setName(createRandomString());
        bankDetailsDTO.setLicenses(new HashSet<>());
        bankDetailsDTO.setCertificates(new HashSet<>());
        return bankDetailsDTO;
    }

    public static BankDetails createBankDetails() {
        BankDetails bankDetails = new BankDetails();
        bankDetails.setId(createRandomLong());
        bankDetails.setBik(createRandomLong());
        bankDetails.setInn(createRandomLong());
        bankDetails.setKpp(createRandomLong());
        bankDetails.setCorAccount(Conversions.intValue(createRandomLong()));
        bankDetails.setCity(createRandomString());
        bankDetails.setJointStockCompany(createRandomString());
        bankDetails.setName(createRandomString());
        bankDetails.setLicenses(new HashSet<>());
        bankDetails.setCertificates(new HashSet<>());
        return bankDetails;
    }

    public static License createLicense() {
        License license = new License();
        license.setId(createRandomLong());
        license.setPhoto(PHOTO);
        license.setBankDetailsLicense(BANK_DETAILS);
        return license;
    }

    public static LicenseDTO createLicenseDTO() {
        LicenseDTO license = new LicenseDTO();
        license.setId(createRandomLong());
        license.setPhoto(PHOTO);
        license.setBankDetailsLicense(BANK_DETAILS);
        return license;
    }

    public static Certificate createCertificate() {
        Certificate certificate = new Certificate();
        certificate.setId(createRandomLong());
        certificate.setPhoto(PHOTO);
        certificate.setBankDetailsCertificate(BANK_DETAILS);
        return certificate;
    }

    public static CertificateDTO createCertificateDTO() {
        CertificateDTO certificate = new CertificateDTO();
        certificate.setId(createRandomLong());
        certificate.setPhoto(PHOTO);
        certificate.setBankDetailsCertificate(BANK_DETAILS);
        return certificate;
    }

    private static Long createRandomLong() {
        Random random = new Random();
        return random.nextLong(1000L);
    }

    private static String createRandomString() {
        return RandomStringUtils.random(15, true, true);
    }
}
