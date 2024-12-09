package com.bank.publicinfo.dto;

import com.bank.publicinfo.entity.Branch;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalTime;

@Data
public class AtmDTO implements Auditable {


    private Long id;

    @NotNull(message = "Вы не указали address")
    @Size(min = 1, max = 370, message = "Вы вышли за допустимый диапозо от 1 до 370 ")
    private String address;

    private LocalTime startOfWork;

    private LocalTime endOfWork;

    @NotNull(message = "Вы не указали allHours")
    private Boolean allHours;

    private Branch branch;

    private Boolean isDeleteBranch;
}
