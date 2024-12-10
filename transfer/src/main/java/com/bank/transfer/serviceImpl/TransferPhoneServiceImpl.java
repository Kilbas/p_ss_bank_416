package com.bank.transfer.serviceImpl;

import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.repository.TransferPhoneRepository;
import com.bank.transfer.service.TransferPhoneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(rollbackFor = EntityNotFoundException.class)
@RequiredArgsConstructor
@Slf4j
public class TransferPhoneServiceImpl implements TransferPhoneService {
    private final TransferPhoneRepository transferPhoneRepository;

    @Override
    public PhoneTransfer addPhoneTransfer(PhoneTransfer phoneTransfer) {
        transferPhoneRepository.save(phoneTransfer);

        return phoneTransfer;
    }

    @Override
    public void deletePhoneTransfer(long id) {
        PhoneTransfer phoneTransfer = transferPhoneRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        transferPhoneRepository.delete(phoneTransfer);
    }

    @Override
    public PhoneTransfer updatePhoneTransfer(PhoneTransfer phoneTransfer, long id) {
        PhoneTransfer existingTransfer = transferPhoneRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        existingTransfer.setAmount(phoneTransfer.getAmount());
        existingTransfer.setNumber(phoneTransfer.getNumber());
        existingTransfer.setPurpose(phoneTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(phoneTransfer.getAccountDetailsId());

        transferPhoneRepository.save(existingTransfer);

        return existingTransfer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneTransfer> getAllPhoneTransfers() {
        return transferPhoneRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PhoneTransfer getPhoneTransferById(long phoneTransferId) {
        return transferPhoneRepository.findById(phoneTransferId)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(phoneTransferId)
        );
    }

    private EntityNotFoundException logAndThrowEntityNotFoundException(long id) {
        log.error("Не найден PhoneTransfer с указанным id {}", id);

        return new EntityNotFoundException("Не найден PhoneTransfer с id" + id);
    }
}
