package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.BankDetails;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
public class LicenseDTO implements Auditable {

    private Long id;

    @NotNull(message = "Вы не указали photo")
    private byte[] photo;

    @NotNull(message = "Вы не указали Id bankDetails")
    private BankDetails bankDetailsLicense;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LicenseDTO that = (LicenseDTO) o;
        if (that.getId()==null) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}