package com.bank.transfer.service;

import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.repository.TransferPhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
    public void deletePhoneTransfer(long id) {
        transferPhoneRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updatePhoneTransfer(PhoneTransfer phoneTransfer, long id) {
        PhoneTransfer existingTransfer = transferPhoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CardTransfer с id " + id + " не найден"));

        existingTransfer.setAmount(phoneTransfer.getAmount());
        existingTransfer.setNumber(phoneTransfer.getNumber());
        existingTransfer.setPurpose(phoneTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(phoneTransfer.getAccountDetailsId());

        transferPhoneRepository.save(existingTransfer);
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
                new EntityNotFoundException("CardTransfer с id " + phoneTransferId + " не найден"));
    }
}
