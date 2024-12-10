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
        CardTransfer cardTransfer = findCardTransferOrThrowException(id);

        transferCardRepository.delete(cardTransfer);
    }

    @Override
    public CardTransfer updateCardTransfer(CardTransfer cardTransfer, long id) {
        CardTransfer existingTransfer = findCardTransferOrThrowException(id);

        copyFieldsFromUpdatedEntity(existingTransfer, cardTransfer);

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
        return findCardTransferOrThrowException(cardTransferId);
    }

    private CardTransfer findCardTransferOrThrowException(long id) {
        return transferCardRepository.findById(id)
                .orElseThrow(() -> logAndThrowEntityNotFoundException(id));
    }

    private EntityNotFoundException logAndThrowEntityNotFoundException(long id) {
        log.error("Не найден CardTransfer с указанным id {}", id);

        return new EntityNotFoundException("Не найден CardTransfer с id" + id);
    }

    private void copyFieldsFromUpdatedEntity(CardTransfer existingTransfer,
                                             CardTransfer updatedTransfer) {
        existingTransfer.setAmount(updatedTransfer.getAmount());
        existingTransfer.setNumber(updatedTransfer.getNumber());
        existingTransfer.setPurpose(updatedTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(updatedTransfer.getAccountDetailsId());
    }
}