package com.bank.history.services;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.mappers.HistoryMapper;
import com.bank.history.models.History;
import com.bank.history.repositories.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper historyMapper) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
    }

    @Override
    public List<HistoryDTO> findAll() {
        return historyMapper.listToDTO(historyRepository.findAll());
    }

    @Override
    public HistoryDTO findById(Long id) {
        return historyMapper.toDTO(historyRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }

    @Transactional
    @Override
    public void update(HistoryDTO historyDTO, Long id) {
        History historyOld = historyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        historyMapper.toEntityUpdate(historyDTO, historyOld);
        historyRepository.save(historyOld);
    }

    @Transactional
    public void deleteById(Long id) {
        historyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        historyRepository.deleteById(id);
    }

    @Transactional
    public void save(HistoryDTO historyDTO) {
        historyRepository.save(historyMapper.toEntitySave(historyDTO));
    }
}
