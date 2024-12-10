package com.bank.transfer.serviceImpl;

import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.repository.TransferCardRepository;
import com.bank.transfer.service.TransferCardService;
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
public class TransferCardServiceImpl implements TransferCardService {
    private final TransferCardRepository transferCardRepository;

    @Override
    public CardTransfer addCardTransfer(CardTransfer cardTransfer) {
        transferCardRepository.save(cardTransfer);

        return cardTransfer;
    }

    @Override
    public void deleteCardTransfer(long id) {
        CardTransfer cardTransfer = transferCardRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        transferCardRepository.delete(cardTransfer);
    }

    @Override
    public CardTransfer updateCardTransfer(CardTransfer cardTransfer, long id) {
        CardTransfer existingTransfer = transferCardRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));

        existingTransfer.setAmount(cardTransfer.getAmount());
        existingTransfer.setNumber(cardTransfer.getNumber());
        existingTransfer.setPurpose(cardTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(cardTransfer.getAccountDetailsId());

        transferCardRepository.save(existingTransfer);

        return existingTransfer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardTransfer> getAllCardTransfers() {
        return transferCardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CardTransfer getCardTransferById(long cardTransferId) {
        return transferCardRepository.findById(cardTransferId)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(cardTransferId));
    }

    private EntityNotFoundException logAndThrowEntityNotFoundException(long id) {
        log.error("Не найден CardTransfer с указанным id {}", id);

        return new EntityNotFoundException("Не найден CardTransfer с id" + id);
    }
}
