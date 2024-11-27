package com.bank.transfer.service;

import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.repository.TransferPhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferPhoneServiceImpl implements TransferPhoneService {
    private final TransferPhoneRepository transferPhoneRepository;

    @Override
    @Transactional
    public void addPhoneTransfer(PhoneTransfer phoneTransfer) {
        transferPhoneRepository.save(phoneTransfer);
    }

    @Override
    @Transactional
    public void deletePhoneTransfer(PhoneTransfer phoneTransfer) {
        transferPhoneRepository.delete(phoneTransfer);
    }

    @Override
    @Transactional
    public void updatePhoneTransfer(PhoneTransfer phoneTransfer) {
        transferPhoneRepository.save(phoneTransfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneTransfer> getAllPhoneTransfers() {
        return transferPhoneRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PhoneTransfer getPhoneTransferById(long phoneTransferId) {
        return transferPhoneRepository.findById(phoneTransferId).orElseThrow(() ->
                new NotFoundException("phone transfer not found"));
    }
}
