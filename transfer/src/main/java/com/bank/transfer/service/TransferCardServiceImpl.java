package com.bank.transfer.service;

import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.repository.TransferCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
//@RequiredArgsConstructor
public class TransferCardServiceImpl implements TransferCardService {

    @Override
    public void addCardTransfer(CardTransfer phoneTransfer) {

    }

    @Override
    public void deleteCardTransfer(CardTransfer phoneTransfer) {

    }

    @Override
    public void updateCardTransfer(CardTransfer phoneTransfer) {

    }

    @Override
    public List<CardTransfer> getAllCardTransfer() {
        return List.of();
    }

    @Override
    public CardTransfer getCardTransferById(long phoneTransferId) {
        return null;
    }
}
