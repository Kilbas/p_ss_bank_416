package com.bank.antifraud.service;

import com.bank.antifraud.dto.SuspiciousAccountTransfersDTO;
import com.bank.antifraud.entity.SuspiciousAccountTransfers;
import com.bank.antifraud.repository.SuspiciousAccountTransfersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

import static com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper.toDTO;
import static com.bank.antifraud.mapper.SuspiciousAccountTransfersMapper.toEntity;

@Service
public class SuspiciousAccountTransfersServiceImpl implements SuspiciousAccountTransfersService {
    private final SuspiciousAccountTransfersRepository repository;

    @Autowired
    public SuspiciousAccountTransfersServiceImpl(SuspiciousAccountTransfersRepository suspiciousAccountTransfersRepository) {
        this.repository = suspiciousAccountTransfersRepository;
    }


    @Override
    public SuspiciousAccountTransfersDTO create(SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers entity = toEntity(transferDTO);
        entity = repository.save(entity);
        return toDTO(entity);
    }


    @Override
    public SuspiciousAccountTransfersDTO update(Long id, SuspiciousAccountTransfersDTO transferDTO) {
        SuspiciousAccountTransfers existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transfer not found"));
        existing.setAccountTransferId(transferDTO.getAccountTransferId());
        existing.setBlocked(transferDTO.getIsBlocked());
        existing.setSuspicious(transferDTO.getIsSuspicious());
        existing.setBlockedReason(transferDTO.getBlockedReason());
        existing.setSuspiciousReason(transferDTO.getSuspiciousReason());
        return toDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public SuspiciousAccountTransfersDTO findById(Long id) {
        return null;
    }

    @Override
    public List<SuspiciousAccountTransfersDTO> findAll() {
        return List.of();
    }
}
