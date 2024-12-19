package com.bank.history.services;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.mappers.HistoryMapper;
import com.bank.history.models.History;
import com.bank.history.repositories.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/**
 * <h3>Назначение класса:</h3>
 * Этот класс служит для реализации интерфейса {@link com.bank.history.services.HistoryService}.
 * Обеспечивает выполнение CRUD операций в базе данных.
 * Для выполнения операций используются экземпляры {@link com.bank.history.repositories.HistoryRepository}
 * и {@link com.bank.history.mappers.HistoryMapper}.
 * <br>
 *
 * @author Амир Турпуханов
 * @version v1
 */

@RequiredArgsConstructor
@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;

    /**
     * <h3>findAll()</h3>
     * Метод возвращает все записи из таблицы.
     *
     * @return List<HistoryDTO> - лист всех entity, конвертированных в DTO.
     */

    @Transactional(readOnly = true)
    @Override
    public Page<HistoryDTO> findAll(Pageable pageable) {
        return historyMapper.pageToDTO(historyRepository.findAll(pageable));
    }

    /**
     * <h3>findById(Long id)</h3>
     * Метод ищет запись в таблице по ее ID
     *
     * @param id ID искомой записи.
     * @return экземпляр HistoryDTO, полученный после конвертации найденной записи.
     */

    @Transactional(readOnly = true)
    @Override
    public HistoryDTO findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("в метод findById передан null!");
        }
        return historyMapper.toDTO(historyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("History c id %d не найдена", id))));
    }

    /**
     * <h3>update(HistoryDTO historyDTO, Long id)</h3>
     * Метод изменяет запись в таблице
     *
     * @param historyDTO Принимает DTO объект с нужными изменениями. <u>Не все поля обязаны присутствовать</u>.
     * @param id         ID той записи, которую нужно обновить.
     */

    @Transactional
    @Override
    public void update(HistoryDTO historyDTO, Long id) {
        if (historyDTO == null || id == null) {
            throw new IllegalArgumentException("В метод update передан null!");
        }
        History historyOld = historyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("History c id %d не найдена", id)));
        historyMapper.toEntityUpdate(historyDTO, historyOld);
        historyRepository.save(historyOld);
    }

    /**
     * <h3>deleteById(Long id)</h3>
     * Метод удаляет запись в таблице по ID.
     *
     * @param id ID удаляемой записи в таблице.
     */

    @Transactional
    public void deleteById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("В метод deleteById передан null!");
        }
        historyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("History c id %d не найдена", id)));
        historyRepository.deleteById(id);
    }

    /**
     * <h3>save(HistoryDTO historyDTO)</h3>
     * Метод сохраняет запись в базе данных
     *
     * @param historyDTO объект DTO, соответствующий той entity, которую мы хотим сохранить.
     */

    @Transactional
    public History save(HistoryDTO historyDTO) {
        if (historyDTO == null) {
            throw new IllegalArgumentException("В метод save передан null!");
        }
        return historyRepository.save(historyMapper.toEntitySave(historyDTO));
    }
}
