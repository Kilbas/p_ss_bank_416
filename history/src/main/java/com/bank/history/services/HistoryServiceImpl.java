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
/**
 * <h3>Назначение класса:</h3>
 * Этот класс служит для реализации интерфейса {@link com.bank.history.services.HistoryService}.
 * Обеспечивает выполнение CRUD операций в базе данных.
 * Для выполнения операций используются экземпляры {@link com.bank.history.repositories.HistoryRepository} и {@link com.bank.history.mappers.HistoryMapper}.
 * <br>
 * @author Амир Турпуханов
 * @version v1
 * */
@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final HistoryMapper historyMapper;
    /**
     * @param historyRepository используется для доступа к БД. Получаем из Spring Context.
     * @param historyMapper используется для конвертации entity в dto и обратно. Получаем из Spring Context.
     * */
    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository, HistoryMapper historyMapper) {
        this.historyRepository = historyRepository;
        this.historyMapper = historyMapper;
    }

    /**
     * <h3>findAll()</h3>
     * Метод возвращает все записи из таблицы.
     * @return List<HistoryDTO> - лист всех entity, конвертированных в DTO.
     * */

    @Transactional
    @Override
    public List<HistoryDTO> findAll() {
        return historyMapper.listToDTO(historyRepository.findAll());
    }
    /**
     * <h3>findById(Long id)</h3>
     * Метод ищет запись в таблице по ее ID
     * @param id ID искомой записи.
     * @return экземпляр HistoryDTO, полученный после конвертации найденной записи.
     * */
    @Transactional
    @Override
    public HistoryDTO findById(Long id) {
        return historyMapper.toDTO(historyRepository.findById(id).orElseThrow(EntityNotFoundException::new));
    }
    /**
     * <h3>update(HistoryDTO historyDTO, Long id)</h3>
     * Метод изменяет запись в таблице
     * @param historyDTO Принимает DTO объект с нужными изменениями. <u>Не все поля обязаны присутствовать</u>.
     * @param id ID той записи, которую нужно обновить.
     * */

    @Transactional
    @Override
    public void update(HistoryDTO historyDTO, Long id) {
        History historyOld = historyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        historyMapper.toEntityUpdate(historyDTO, historyOld);
        historyRepository.save(historyOld);
    }
    /**
     * <h3>deleteById(Long id)</h3>
     * Метод удаляет запись в таблице по ID.
     * @param id ID удаляемой записи в таблице.
     * */
    @Transactional
    public void deleteById(Long id) {
        historyRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        historyRepository.deleteById(id);
    }
    /**
     * <h3>save(HistoryDTO historyDTO)</h3>
     * Метод сохраняет запись в базе данных
     * @param historyDTO объект DTO, соответствующий той entity, которую мы хотим сохранить.
     * */
    @Transactional
    public void save(HistoryDTO historyDTO) {
        historyRepository.save(historyMapper.toEntitySave(historyDTO));
    }
}
