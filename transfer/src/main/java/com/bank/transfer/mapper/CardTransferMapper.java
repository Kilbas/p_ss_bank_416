package com.bank.transfer.mapper;

import com.bank.transfer.dto.AccountTransferDTO;
import com.bank.transfer.dto.CardTransferDTO;
import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.model.CardTransfer;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CardTransferMapper {
    CardTransfer dtoToCardTransfer (CardTransferDTO cardTransferDTO);
    CardTransferDTO cardTransferToDTO (CardTransfer cardTransfer);
}
