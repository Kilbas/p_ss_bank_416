package com.bank.antifraud.mapper;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-13T14:11:53+0100",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8 (Eclipse Adoptium)"
)
@Component
public class SuspiciousAccountTransfersMapperImpl implements SuspiciousAccountTransfersMapper {

    @Override
    public SuspiciousAccountTransfersDTO toDTO(SuspiciousAccountTransfers suspiciousAccountTransfers) {
        if ( suspiciousAccountTransfers == null ) {
            return null;
        }

        SuspiciousAccountTransfersDTO.SuspiciousAccountTransfersDTOBuilder suspiciousAccountTransfersDTO = SuspiciousAccountTransfersDTO.builder();

        suspiciousAccountTransfersDTO.id( suspiciousAccountTransfers.getId() );
        suspiciousAccountTransfersDTO.accountTransferId( suspiciousAccountTransfers.getAccountTransferId() );
        suspiciousAccountTransfersDTO.blockedReason( suspiciousAccountTransfers.getBlockedReason() );
        suspiciousAccountTransfersDTO.suspiciousReason( suspiciousAccountTransfers.getSuspiciousReason() );

        return suspiciousAccountTransfersDTO.build();
    }

    @Override
    public SuspiciousAccountTransfers toEntity(SuspiciousAccountTransfersDTO suspiciousAccountTransfersDTO) {
        if ( suspiciousAccountTransfersDTO == null ) {
            return null;
        }

        SuspiciousAccountTransfers.SuspiciousAccountTransfersBuilder suspiciousAccountTransfers = SuspiciousAccountTransfers.builder();

        suspiciousAccountTransfers.id( suspiciousAccountTransfersDTO.getId() );
        suspiciousAccountTransfers.accountTransferId( suspiciousAccountTransfersDTO.getAccountTransferId() );
        suspiciousAccountTransfers.blocked( suspiciousAccountTransfersDTO.isBlocked() );
        suspiciousAccountTransfers.suspicious( suspiciousAccountTransfersDTO.isSuspicious() );
        suspiciousAccountTransfers.blockedReason( suspiciousAccountTransfersDTO.getBlockedReason() );
        suspiciousAccountTransfers.suspiciousReason( suspiciousAccountTransfersDTO.getSuspiciousReason() );

        return suspiciousAccountTransfers.build();
    }

    @Override
    public void updateFromDto(SuspiciousAccountTransfersDTO transferDTO, SuspiciousAccountTransfers existing) {
        if ( transferDTO == null ) {
            return;
        }

        existing.setAccountTransferId( transferDTO.getAccountTransferId() );
        existing.setBlocked( transferDTO.isBlocked() );
        existing.setSuspicious( transferDTO.isSuspicious() );
        existing.setBlockedReason( transferDTO.getBlockedReason() );
        existing.setSuspiciousReason( transferDTO.getSuspiciousReason() );
    }
}
