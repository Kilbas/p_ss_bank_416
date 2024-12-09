package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.Atm;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;
import java.util.Set;

@Data
public class BranchDTO implements Auditable{

    private Long id;

    @NotNull(message = "Вы не указали address")
    @Size(min = 1, max = 370, message = "Вы вышли за допустимый диапозо от 1 до 370 ")
    private String address;

    @NotNull(message = "Вы не указали phoneNumber")
    @Min(value = 0, message = "Kpp не может быть отрицательным")
    private Long phoneNumber;

    @NotNull(message = "Вы не указали city")
    @Size(min = 1, max = 250, message = "Вы вышли за допустимый диапозо от 1 до 250 ")
    private String city;

    @NotNull(message = "Вы не указали startOfWork")
    private LocalTime startOfWork;

    @NotNull(message = "Вы не указали endOfWork")
    private LocalTime endOfWork;

    private Set<Atm> atms;

    private Boolean isDeleteAtm;
}
