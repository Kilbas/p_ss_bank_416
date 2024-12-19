package com.bank.history.services;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.models.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HistoryService {

    Page<HistoryDTO> findAll(Pageable pageable);

    HistoryDTO findById(Long id);

    void update(HistoryDTO historyDTO, Long id);

    void deleteById(Long id);

    History save(HistoryDTO historyDTO);
}
