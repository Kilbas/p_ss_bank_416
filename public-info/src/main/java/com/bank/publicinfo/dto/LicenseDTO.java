package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.BankDetails;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class LicenseDTO implements Auditable {

    private Long id;

    @NotNull(message = "Вы не указали photo")
    private byte[] photo;

    @NotNull(message = "Вы не указали Id bankDetails")
    private BankDetails bankDetailsLicense;
}
