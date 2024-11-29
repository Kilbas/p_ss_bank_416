package com.bank.transfer.mapper;

import com.bank.transfer.dto.PhoneTransferDTO;
import com.bank.transfer.model.PhoneTransfer;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

public class PhoneTransferMapper {
    private final static ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.addMappings(new PropertyMap<PhoneTransferDTO, PhoneTransfer>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
    }

    public static PhoneTransfer dtoToPhoneTransfer (PhoneTransferDTO phoneTransferDTO) {
        return modelMapper.map(phoneTransferDTO, PhoneTransfer.class);
    }

    public static PhoneTransferDTO phoneTransferToDTO (PhoneTransfer phoneTransfer) {
        return modelMapper.map(phoneTransfer, PhoneTransferDTO.class);
    }
}
