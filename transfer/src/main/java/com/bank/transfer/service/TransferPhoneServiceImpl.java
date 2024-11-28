package com.bank.transfer.service;

import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.repository.TransferPhoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TransferPhoneServiceImpl implements TransferPhoneService {
    private final TransferPhoneRepository transferPhoneRepository;

    @Override
    @Transactional
    public void addPhoneTransfer(PhoneTransfer phoneTransfer) {
        transferPhoneRepository.save(phoneTransfer);
        log.info("Трансфер PhoneTransfer успешно добавлен");
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deletePhoneTransfer(long id) {
        PhoneTransfer accountTransfer = transferPhoneRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNitFoundException(id));

        transferPhoneRepository.delete(accountTransfer);
        log.info("Удален PhoneTransfer с id {}", id);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void updatePhoneTransfer(PhoneTransfer phoneTransfer, long id) {
        PhoneTransfer existingTransfer = transferPhoneRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNitFoundException(id));

        existingTransfer.setAmount(phoneTransfer.getAmount());
        existingTransfer.setNumber(phoneTransfer.getNumber());
        existingTransfer.setPurpose(phoneTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(phoneTransfer.getAccountDetailsId());

        transferPhoneRepository.save(existingTransfer);
        log.info("Обновлен PhoneTransfer c id {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhoneTransfer> getAllPhoneTransfers() {
        log.info("Получение всех PhoneTransfer");
        return transferPhoneRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = EntityNotFoundException.class)
    public PhoneTransfer getPhoneTransferById(long phoneTransferId) {
        return transferPhoneRepository.findById(phoneTransferId)
                .orElseThrow(() -> logAndThrowEntityNitFoundException(phoneTransferId)
        );
    }

    private EntityNotFoundException logAndThrowEntityNitFoundException(long id) {
        log.error("Не найден PhoneTransfer с указанным id {}", id);
        return new EntityNotFoundException("Не найден PhoneTransfer с id" + id);
    }
}
