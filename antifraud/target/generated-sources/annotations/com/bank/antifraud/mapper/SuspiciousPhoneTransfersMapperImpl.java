package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-13T14:11:53+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Eclipse Adoptium)"
)
@Component
public class SuspiciousPhoneTransfersMapperImpl implements SuspiciousPhoneTransfersMapper {

    @Override
    public SuspiciousPhoneTransferDTO toDTO(SuspiciousPhoneTransfers suspiciousPhoneTransfers) {
        if ( suspiciousPhoneTransfers == null ) {
            return null;
        }

        SuspiciousPhoneTransferDTO.SuspiciousPhoneTransferDTOBuilder suspiciousPhoneTransferDTO = SuspiciousPhoneTransferDTO.builder();

        suspiciousPhoneTransferDTO.id( suspiciousPhoneTransfers.getId() );
        suspiciousPhoneTransferDTO.phoneTransferId( suspiciousPhoneTransfers.getPhoneTransferId() );
        suspiciousPhoneTransferDTO.blockedReason( suspiciousPhoneTransfers.getBlockedReason() );
        suspiciousPhoneTransferDTO.suspiciousReason( suspiciousPhoneTransfers.getSuspiciousReason() );

        return suspiciousPhoneTransferDTO.build();
    }

    @Override
    public SuspiciousPhoneTransfers toEntity(SuspiciousPhoneTransferDTO suspiciousPhoneTransferDTO) {
        if ( suspiciousPhoneTransferDTO == null ) {
            return null;
        }

        SuspiciousPhoneTransfers.SuspiciousPhoneTransfersBuilder suspiciousPhoneTransfers = SuspiciousPhoneTransfers.builder();

        suspiciousPhoneTransfers.id( suspiciousPhoneTransferDTO.getId() );
        suspiciousPhoneTransfers.phoneTransferId( suspiciousPhoneTransferDTO.getPhoneTransferId() );
        suspiciousPhoneTransfers.blocked( suspiciousPhoneTransferDTO.isBlocked() );
        suspiciousPhoneTransfers.suspicious( suspiciousPhoneTransferDTO.isSuspicious() );
        suspiciousPhoneTransfers.blockedReason( suspiciousPhoneTransferDTO.getBlockedReason() );
        suspiciousPhoneTransfers.suspiciousReason( suspiciousPhoneTransferDTO.getSuspiciousReason() );

        return suspiciousPhoneTransfers.build();
    }

    @Override
    public void updateFromDto(SuspiciousPhoneTransferDTO dto, SuspiciousPhoneTransfers suspiciousPhoneTransfers) {
        if ( dto == null ) {
            return;
        }

        suspiciousPhoneTransfers.setPhoneTransferId( dto.getPhoneTransferId() );
        suspiciousPhoneTransfers.setBlocked( dto.isBlocked() );
        suspiciousPhoneTransfers.setSuspicious( dto.isSuspicious() );
        suspiciousPhoneTransfers.setBlockedReason( dto.getBlockedReason() );
        suspiciousPhoneTransfers.setSuspiciousReason( dto.getSuspiciousReason() );
    }
}
