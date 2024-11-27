package com.bank.transfer.service;

import com.bank.transfer.model.PhoneTransfer;

import java.util.List;

public interface TransferPhoneService {
    void addPhoneTransfer(PhoneTransfer phoneTransfer);
    void deletePhoneTransfer(PhoneTransfer phoneTransfer);
    void updatePhoneTransfer(PhoneTransfer phoneTransfer);
    List<PhoneTransfer> getAllPhoneTransfers();
    PhoneTransfer getPhoneTransferById(long phoneTransferId);
}
