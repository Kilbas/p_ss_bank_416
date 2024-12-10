package com.bank.publicinfo.service.atm;

import com.bank.publicinfo.dto.AtmDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AtmService {

    List<AtmDTO> getAllAtms(Pageable pageable);

    AtmDTO getAtm(Long id);

    AtmDTO addAtm(AtmDTO atm);

    AtmDTO updateAtm(Long id, AtmDTO updateAtm);

    void deleteAtm(Long id);
}