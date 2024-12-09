package com.bank.history.services;

import com.bank.history.dto.HistoryDTO;

import java.util.List;

public interface HistoryService {

    List<HistoryDTO> findAll();

    HistoryDTO findById(Long id);

    void update(HistoryDTO historyDTO, Long id);

    void deleteById(Long id);

    void save(HistoryDTO historyDTO);
}
