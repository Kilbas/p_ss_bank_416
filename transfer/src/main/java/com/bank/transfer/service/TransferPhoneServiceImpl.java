package com.bank.transfer.service;

import com.bank.transfer.model.PhoneTransfer;
import com.bank.transfer.repository.TransferPhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
//@RequiredArgsConstructor
public class TransferPhoneServiceImpl implements TransferPhoneService {

    @Override
    public void addPhoneTransfer(PhoneTransfer phoneTransfer) {

    }

    @Override
    public void deletePhoneTransfer(PhoneTransfer phoneTransfer) {

    }

    @Override
    public void updatePhoneTransfer(PhoneTransfer phoneTransfer) {

    }

    @Override
    public List<PhoneTransfer> getAllPhoneTransfers() {
        return List.of();
    }

    @Override
    public PhoneTransfer getPhoneTransferById(long phoneTransferId) {
        return null;
    }
}
