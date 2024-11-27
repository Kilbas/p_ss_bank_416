package com.bank.transfer.service;

import com.bank.transfer.model.AccountTransfer;
import com.bank.transfer.model.CardTransfer;
import com.bank.transfer.model.PhoneTransfer;

import java.util.List;

public interface TransferPhoneService {
    void addPhoneTransfer(PhoneTransfer phoneTransfer);
    void deletePhoneTransfer(long id);
    void updatePhoneTransfer(PhoneTransfer phoneTransfer, long id);
    List<PhoneTransfer> getAllPhoneTransfers();
    PhoneTransfer getPhoneTransferById(long phoneTransferId);
}
