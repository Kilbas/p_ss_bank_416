package com.bank.transfer.service;

import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.repository.TransferCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

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
    public void deleteCardTransfer(CardTransfer cardTransfer) {
        transferCardRepository.delete(cardTransfer);
    }

    @Override
    @Transactional
    public void updateCardTransfer(CardTransfer cardTransfer) {
        transferCardRepository.save(cardTransfer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardTransfer> getAllCardTransfer() {
        return transferCardRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CardTransfer getCardTransferById(long cardTransferId) {
        return transferCardRepository.findById(cardTransferId).orElseThrow(() ->
                new NotFoundException("card transfer not found"));
    }
}
