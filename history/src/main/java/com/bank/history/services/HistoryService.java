package com.bank.history.services;

import com.bank.history.dto.HistoryDTO;

import java.util.List;

public interface HistoryService {

    List<HistoryDTO> findAll();

    HistoryDTO findById(int id);

    void update(HistoryDTO historyDTO, int id);

    void deleteById(int id);

    void save(HistoryDTO historyDTO);
}
