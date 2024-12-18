package com.bank.history.services;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.models.History;

import java.util.List;

public interface HistoryService {

    List<HistoryDTO> findAll();

    HistoryDTO findById(Long id);

    void update(HistoryDTO historyDTO, Long id);

    void deleteById(Long id);

    History save(HistoryDTO historyDTO);
}
