package com.bank.transfer.service;


import com.bank.transfer.model.CardTransfer;

import java.util.List;

public interface TransferCardService {
    void addCardTransfer(CardTransfer cardTransfer);
    void deleteCardTransfer(CardTransfer cardTransfer);
    void updateCardTransfer(CardTransfer cardTransfer);
    List<CardTransfer> getAllCardTransfer();
    CardTransfer getCardTransferById(long cardTransferId);
}
