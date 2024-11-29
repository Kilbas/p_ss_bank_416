package com.bank.transfer.mapper;

import com.bank.transfer.dto.AccountTransferDTO;
import com.bank.transfer.model.AccountTransfer;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class AccountTransferMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<AccountTransferDTO, AccountTransfer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
    }

    public static AccountTransfer dtoToAccountTransfer (AccountTransferDTO accountTransferDTO) {
        return modelMapper.map(accountTransferDTO, AccountTransfer.class);
    }

    public static AccountTransferDTO accountTransferToDTO (AccountTransfer accountTransfer) {
        return modelMapper.map(accountTransfer, AccountTransferDTO.class);
    }
}
