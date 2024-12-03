package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousCardTransferDTO;
import com.bank.antifraud.entity.SuspiciousCardTransfer;
import com.bank.antifraud.mapper.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuspiciousCardTransferServiceImpl implements SuspiciousCardTransferService {

    private final SuspiciousCardTransferRepository repository;
    private final SuspiciousCardTransferMapper mapper;

    public SuspiciousCardTransferServiceImpl(SuspiciousCardTransferRepository repository,
                                             SuspiciousCardTransferMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public SuspiciousCardTransferDTO findById (Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Suspicious account transfer not found with id: " + id));
    }

    @Override
    public List<SuspiciousCardTransferDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public SuspiciousCardTransferDTO create(SuspiciousCardTransferDTO transferDTO) {
        SuspiciousCardTransfer entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public SuspiciousCardTransferDTO update(Long id, SuspiciousCardTransferDTO transferDTO) {
        SuspiciousCardTransfer existing = repository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Transfer not found" + id));
        existing.setIsBlocked(transferDTO.getIsBlocked());
        existing.setIsSuspicious(transferDTO.getIsSuspicious());
        existing.setBlockedReason(transferDTO.getBlockedReason());
        existing.setSuspiciousReason(transferDTO.getSuspiciousReason());
        return mapper.toDTO(repository.save(existing));


    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Suspicious card transfer not found with id: " + id);
        }
        repository.deleteById(id);
    }


    @Override
    public List<SuspiciousCardTransferDTO> findBlockedTransfers() {
        return repository.findAll().stream()
                .filter(SuspiciousCardTransfer::getIsBlocked)
                .map(mapper::toDTO)
                .toList();
    }
}
