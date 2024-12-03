package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuspiciousAccountTransfersServiceImpl implements SuspiciousAccountTransfersService {
    private final SuspiciousAccountTransfersRepository repository;
    private final SuspiciousAccountTransfersMapper mapper;

    @Autowired
    public SuspiciousAccountTransfersServiceImpl(SuspiciousAccountTransfersRepository repository,
                                                 SuspiciousAccountTransfersMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    @Override
    public SuspiciousAccountTransfersDTO create(SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers entity = mapper.toEntity(transferDTO);
        entity = repository.save(entity);
        return mapper.toDTO(entity);
    }


    @Override
    public SuspiciousAccountTransfersDTO update(Long id, SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found" + id));
        existing.setBlocked(transferDTO.isBlocked());
        existing.setSuspicious(transferDTO.isSuspicious());
        existing.setBlockedReason(transferDTO.getBlockedReason());
        existing.setSuspiciousReason(transferDTO.getSuspiciousReason());
        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Transfer not found" + id);
        }
        repository.deleteById(id);
    }

    @Override
    public SuspiciousAccountTransfersDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
