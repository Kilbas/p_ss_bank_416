package com.bank.transfer.service;

import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.repository.TransferCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferCardServiceImpl implements TransferCardService {
    private final TransferCardRepository transferCardRepository;

    @Override
    @Transactional
    public void addCardTransfer(CardTransfer cardTransfer) {
        transferCardRepository.save(cardTransfer);
    }

    @Override
    @Transactional
    public void deleteCardTransfer(long id) {
        transferCardRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateCardTransfer(CardTransfer cardTransfer, long id) {
        CardTransfer existingTransfer = transferCardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CardTransfer с id " + cardTransfer.getId() + " не найден"));

        existingTransfer.setAmount(cardTransfer.getAmount());
        existingTransfer.setNumber(cardTransfer.getNumber());
        existingTransfer.setPurpose(cardTransfer.getPurpose());
        existingTransfer.setAccountDetailsId(cardTransfer.getAccountDetailsId());

        transferCardRepository.save(existingTransfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardTransfer> getAllCardTransfers() {
        return transferCardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CardTransfer getCardTransferById(long cardTransferId) {
        return transferCardRepository.findById(cardTransferId).orElseThrow(() ->
                new EntityNotFoundException("CardTransfer с id " + cardTransferId + " не найден"));
    }
}
