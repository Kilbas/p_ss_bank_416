package com.bank.transfer.mapper;

import com.bank.transfer.dto.AccountTransferDTO;
import com.bank.transfer.dto.PhoneTransferDTO;
import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.model.PhoneTransfer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PhoneTransferMapper {
    PhoneTransfer dtoToPhoneTransfer (PhoneTransferDTO phoneTransferDTO);
    PhoneTransferDTO phoneTransferToDTO (PhoneTransfer phoneTransfer);
}
