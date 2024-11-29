package com.bank.transfer.mapper;

import com.bank.transfer.dto.TransferDTO;
import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.model.PhoneTransfer;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import static org.apache.commons.io.IOUtils.skip;

public class TransferMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<TransferDTO, AccountTransfer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<TransferDTO, CardTransfer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });

        modelMapper.addMappings(new PropertyMap<TransferDTO, PhoneTransfer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
    }

    public static AccountTransfer dtoToAccountTransfer (TransferDTO transferDTO) {
        return modelMapper.map(transferDTO, AccountTransfer.class);
    }

    public static CardTransfer dtoToCardTransfer (TransferDTO transferDTO) {
        return modelMapper.map(transferDTO, CardTransfer.class);
    }

    public static PhoneTransfer dtoToPhoneTransfer (TransferDTO transferDTO) {
        return modelMapper.map(transferDTO, PhoneTransfer.class);
    }

    public static TransferDTO accountTransferToDTO (AccountTransfer accountTransfer) {
        return modelMapper.map(accountTransfer, TransferDTO.class);
    }

    public static CardTransfer cardTransferToDTO (CardTransfer cardTransfer) {
        return modelMapper.map(cardTransfer, CardTransfer.class);
    }

    public static PhoneTransfer phoneTransferToDTO (PhoneTransfer phoneTransfer) {
        return modelMapper.map(phoneTransfer, PhoneTransfer.class);
    }
}
