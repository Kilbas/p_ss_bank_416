package com.bank.transfer.mapper;

import com.bank.transfer.dto.TransferDTO;
import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.model.PhoneTransfer;
import org.modelmapper.ModelMapper;

public class TransferMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static AccountTransfer dtoToAccountTransfer (TransferDTO transferDTO) {
        return modelMapper.map(transferDTO, AccountTransfer.class);
    }

    public static CardTransfer dtoToCardTransfer (TransferDTO transferDTO) {
        return modelMapper.map(transferDTO, CardTransfer.class);
    }

    public static PhoneTransfer dtoToPhoneTransfer (TransferDTO transferDTO) {
        return modelMapper.map(transferDTO, PhoneTransfer.class);
    }
}
