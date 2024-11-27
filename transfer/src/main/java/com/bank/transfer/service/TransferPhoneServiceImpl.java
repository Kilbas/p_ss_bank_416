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
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deletePhoneTransfer(long id) {
        PhoneTransfer accountTransfer = transferPhoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccountTransfer с id " + id + " не найден"));

        transferPhoneRepository.delete(accountTransfer);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void updatePhoneTransfer(PhoneTransfer phoneTransfer, long id) {
        PhoneTransfer existingTransfer = transferPhoneRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PhoneTransfer с id " + id + " не найден"));

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
    @Transactional(readOnly = true, rollbackFor = EntityNotFoundException.class)
    public PhoneTransfer getPhoneTransferById(long phoneTransferId) {
        return transferPhoneRepository.findById(phoneTransferId).orElseThrow(() ->
                new EntityNotFoundException("PhoneTransfer с id " + phoneTransferId + " не найден"));
    }
}
