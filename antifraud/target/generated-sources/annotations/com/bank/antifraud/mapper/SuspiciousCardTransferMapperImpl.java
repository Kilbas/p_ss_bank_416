package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-13T14:11:53+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Eclipse Adoptium)"
)
@Component
public class SuspiciousCardTransferMapperImpl implements SuspiciousCardTransferMapper {

    @Override
    public SuspiciousCardTransferDTO toDTO(SuspiciousCardTransfer suspiciousCardTransfer) {
        if ( suspiciousCardTransfer == null ) {
            return null;
        }

        SuspiciousCardTransferDTO.SuspiciousCardTransferDTOBuilder suspiciousCardTransferDTO = SuspiciousCardTransferDTO.builder();

        suspiciousCardTransferDTO.id( suspiciousCardTransfer.getId() );
        suspiciousCardTransferDTO.cardTransferId( suspiciousCardTransfer.getCardTransferId() );
        suspiciousCardTransferDTO.blockedReason( suspiciousCardTransfer.getBlockedReason() );
        suspiciousCardTransferDTO.suspiciousReason( suspiciousCardTransfer.getSuspiciousReason() );

        return suspiciousCardTransferDTO.build();
    }

    @Override
    public SuspiciousCardTransfer toEntity(SuspiciousCardTransferDTO suspiciousCardTransferDTO) {
        if ( suspiciousCardTransferDTO == null ) {
            return null;
        }

        SuspiciousCardTransfer.SuspiciousCardTransferBuilder suspiciousCardTransfer = SuspiciousCardTransfer.builder();

        suspiciousCardTransfer.id( suspiciousCardTransferDTO.getId() );
        suspiciousCardTransfer.cardTransferId( suspiciousCardTransferDTO.getCardTransferId() );
        suspiciousCardTransfer.blocked( suspiciousCardTransferDTO.isBlocked() );
        suspiciousCardTransfer.suspicious( suspiciousCardTransferDTO.isSuspicious() );
        suspiciousCardTransfer.blockedReason( suspiciousCardTransferDTO.getBlockedReason() );
        suspiciousCardTransfer.suspiciousReason( suspiciousCardTransferDTO.getSuspiciousReason() );

        return suspiciousCardTransfer.build();
    }

    @Override
    public void updateFromDto(SuspiciousCardTransferDTO transferDTO, SuspiciousCardTransfer existing) {
        if ( transferDTO == null ) {
            return;
        }

        existing.setCardTransferId( transferDTO.getCardTransferId() );
        existing.setBlocked( transferDTO.isBlocked() );
        existing.setSuspicious( transferDTO.isSuspicious() );
        existing.setBlockedReason( transferDTO.getBlockedReason() );
        existing.setSuspiciousReason( transferDTO.getSuspiciousReason() );
    }
}
