package com.bank.transfer.service;

import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.repository.TransferCardRepository;
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
public class TransferCardServiceImpl implements TransferCardService {
    private final TransferCardRepository transferCardRepository;

    @Override
    @Transactional
    public CardTransfer addCardTransfer(CardTransfer cardTransfer) {
        transferCardRepository.save(cardTransfer);
        log.info("Трансфер CardTransfer успешно добавлен");
        return cardTransfer;
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteCardTransfer(long id) {
        CardTransfer cardTransfer = transferCardRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        transferCardRepository.delete(cardTransfer);
        log.info("Удален CardTransfer с id {}", id);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public CardTransfer updateCardTransfer(CardTransfer cardTransfer, long id) {
        CardTransfer existingTransfer = transferCardRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        existingTransfer.setAmount(cardTransfer.getAmount());
        existingTransfer.setNumber(cardTransfer.getNumber());
        existingTransfer.setPurpose(cardTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(cardTransfer.getAccountDetailsId());

        transferCardRepository.save(existingTransfer);
        log.info("Обновлен CardTransfer с id {}", id);

        return existingTransfer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardTransfer> getAllCardTransfers() {
        log.info("Получение всех CardTransfer");

        return transferCardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = EntityNotFoundException.class)
    public CardTransfer getCardTransferById(long cardTransferId) {
        log.info("Поиск CardTransfer c id {}", cardTransferId);

        return transferCardRepository.findById(cardTransferId)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(cardTransferId));
    }

    private EntityNotFoundException logAndThrowEntityNotFoundException(long id) {
        log.error("Не найден CardTransfer с указанным id {}", id);

        return new EntityNotFoundException("Не найден CardTransfer с id" + id);
    }
}
