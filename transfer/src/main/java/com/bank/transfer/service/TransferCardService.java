package com.bank.transfer.service;

import com.bank.transfer.model.CardTransfer;

import java.util.List;

public interface TransferCardService {
    CardTransfer addCardTransfer(CardTransfer cardTransfer);
    CardTransfer deleteCardTransfer(long id);
    CardTransfer updateCardTransfer(CardTransfer cardTransfer, long id);
    List<CardTransfer> getAllCardTransfers();
    CardTransfer getCardTransferById(long cardTransferId);
}
