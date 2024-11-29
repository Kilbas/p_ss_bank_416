package com.bank.transfer.mapper;

import com.bank.transfer.dto.CardTransferDTO;
import com.bank.transfer.model.CardTransfer;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class CardTransferMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<CardTransferDTO, CardTransfer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
    }

    public static CardTransfer dtoToCardTransfer (CardTransferDTO cardTransferDTO) {
        return modelMapper.map(cardTransferDTO, CardTransfer.class);
    }

    public static CardTransferDTO cardTransferToDTO (CardTransfer cardTransfer) {
        return modelMapper.map(cardTransfer, CardTransferDTO.class);
    }
}
