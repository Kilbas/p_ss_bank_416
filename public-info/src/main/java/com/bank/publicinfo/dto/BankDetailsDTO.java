package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.Certificate;
import com.bank.publicinfo.entity.License;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class BankDetailsDTO implements Auditable {

    private Long id;

    @NotNull(message = "Вы не указали bik")
    @Min(value = 0, message = "Bik не может быть отрицательным")
    private Long bik;

    @NotNull(message = "Вы не указали inn")
    @Min(value = 0, message = "Inn не может быть отрицательным")
    private Long inn;

    @NotNull(message = "Вы не указали kpp")
    @Min(value = 0, message = "Kpp не может быть отрицательным")
    private Long kpp;

    @NotNull(message = "Вы не указали corAccount")
    @Min(value = 0, message = "CorAccount не может быть отрицательным")
    private Integer corAccount;

    @NotNull(message = "Вы не указали city")
    @Size(min = 1, max = 180, message = "Вы вышли за допустимый диапозо от 1 до 180 ")
    private String city;

    @NotNull(message = "Вы не указали jointStockCompany")
    @Size(min = 1,max = 15, message = "Вы вышли за допустимый диапозо от 1 до 15 ")
    private String jointStockCompany;

    @NotNull(message = "Вы не указали name")
    @Size(min = 1, max = 80, message = "Вы вышли за допустимый диапозо от 1 до 80 ")
    private String name;

    private Set<License> licenses;

    private Set<Certificate> certificates;

}
