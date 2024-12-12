package com.bank.transfer.mapper;

import com.bank.transfer.dto.AccountTransferDTO;
import com.bank.transfer.model.AccountTransfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountTransferMapper {
    AccountTransfer dtoToAccountTransfer (AccountTransferDTO accountTransferDTO);
    AccountTransferDTO accountTransferToDTO (AccountTransfer accountTransfer);
}
