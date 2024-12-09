package com.bank.publicinfo.service.atm;

import com.bank.publicinfo.dto.AtmDTO;

import java.util.List;

public interface AtmService {

    List<AtmDTO> getAllAtms();

    AtmDTO getAtm(Long id);

    AtmDTO addAtm(AtmDTO atm);

    AtmDTO updateAtm(Long id, AtmDTO updateAtm);

    AtmDTO deleteAtm(Long id);
}
