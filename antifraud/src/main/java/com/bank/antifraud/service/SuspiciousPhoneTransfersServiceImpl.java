package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDTO;
import com.bank.antifraud.entity.SuspiciousPhoneTransfers;
import com.bank.antifraud.mapper.SuspiciousPhoneTransfersMapper;
import com.bank.antifraud.repository.SuspiciousPhoneTransfersRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuspiciousPhoneTransfersServiceImpl implements SuspiciousPhoneTransfersService {

    private final SuspiciousPhoneTransfersRepository repository;
    private final SuspiciousPhoneTransfersMapper mapper;

    public SuspiciousPhoneTransfersServiceImpl(SuspiciousPhoneTransfersRepository repository,
                                               SuspiciousPhoneTransfersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SuspiciousPhoneTransferDTO findById(Long id) {
        return repository.findById(id).
                map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public SuspiciousPhoneTransferDTO create(SuspiciousPhoneTransferDTO transferDTO) {
        if (transferDTO.getPhoneTransferId() == null) {
            throw new IllegalArgumentException("PhoneTransferId cannot be null");
        }
        SuspiciousPhoneTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public SuspiciousPhoneTransferDTO update(Long id, SuspiciousPhoneTransferDTO transferDTO) {
        SuspiciousPhoneTransfers existing = repository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Transfer not found" + id));
        existing.setBlocked(transferDTO.getIsBlocked());
        existing.setSuspicious(transferDTO.getIsSuspicious());
        existing.setBlockedReason(transferDTO.getBlockedReason());
        existing.setSuspiciousReason(transferDTO.getSuspiciousReason());
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Suspicious phone transfer not found");
        }
        repository.deleteById(id);
    }

    @Override
    public List<SuspiciousPhoneTransferDTO> findTransfersByReason(String reason) {
        return repository.findBySuspiciousReasonContainingIgnoreCase(reason).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
