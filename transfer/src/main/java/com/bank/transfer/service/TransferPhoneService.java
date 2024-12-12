package com.bank.transfer.service;

import com.bank.transfer.model.PhoneTransfer;

import java.util.List;

public interface TransferPhoneService {
    PhoneTransfer addPhoneTransfer(PhoneTransfer phoneTransfer);
    void deletePhoneTransfer(long id);
    PhoneTransfer updatePhoneTransfer(PhoneTransfer phoneTransfer, long id);
    List<PhoneTransfer> getAllPhoneTransfers();
    PhoneTransfer getPhoneTransferById(long phoneTransferId);
}
