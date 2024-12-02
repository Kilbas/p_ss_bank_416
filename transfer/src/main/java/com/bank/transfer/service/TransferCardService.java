package com.bank.transfer.service;

import com.bank.transfer.model.CardTransfer;

import java.util.List;

public interface TransferCardService {
    void addCardTransfer(CardTransfer cardTransfer);
    void deleteCardTransfer(long id);
    CardTransfer updateCardTransfer(CardTransfer cardTransfer, long id);
    List<CardTransfer> getAllCardTransfers();
    CardTransfer getCardTransferById(long cardTransferId);
}
