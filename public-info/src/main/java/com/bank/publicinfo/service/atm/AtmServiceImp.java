package com.bank.publicinfo.service.atm;


import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.repository.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
public class AtmServiceImp implements AtmService {

    private final AtmRepository atmRepository;
    private final AtmMapper mapper;
    private final BranchRepository branchRepository;

    public AtmServiceImp(AtmRepository atmRepository, AtmMapper mapper, BranchRepository branchRepository) {
        this.atmRepository = atmRepository;
        this.mapper = mapper;
        this.branchRepository = branchRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtmDTO> getAllAtms() {
        log.info("Запрос на получение всех банкоматов");
        List<Atm> all = atmRepository.findAll();
        log.info("Получено {} реквизита(ов) банка", all.size());
        return mapper.map(all);
    }

    @Override
    @Transactional(readOnly = true)
    public AtmDTO getAtm(Long id) {
        log.info("Запрос на получение банкомата с id: {}", id);
        Atm atmById = findAtmById(id);
        log.info("Получение банкомат с id: {}", id);
        return mapper.map(atmById);

    }

    @Override
    @AuditAnnotation
    @Transactional
    public AtmDTO deleteAtm(Long id) {
        log.info("Удаление банкомата с ID: {}", id);
        Atm atm = findAtmById(id);
        removeAtmFromBranch(atm);
        atmRepository.deleteById(id);
        log.info("Банкомат с id: {} был успешно удалён", id);
        return mapper.map(atm);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public AtmDTO addAtm(AtmDTO atmDTO) {
        log.info("Добавление нового банкомата");
        Atm atm = mapper.map(atmDTO);
        setBranchForAtm(atm, atmDTO.getBranch());
        handleAllHours(atm);
        Atm save = atmRepository.save(atm);
        log.info("Банкомат успешно добавлен с id: {}", save.getId());
        return mapper.map(save);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public AtmDTO updateAtm(Long id, AtmDTO atmDTO) {
        log.info("Обновление банкомата с id: {}", id);
        Atm atm = findAtmById(id);
        mapper.updateAtmFromDto(atmDTO, atm);
        setBranchForAtm(atm, atmDTO.getBranch());
        handleAllHours(atm);

        if (Boolean.TRUE.equals(atmDTO.getIsDeleteBranch())) {
            atm.setBranch(null);
        }
        Atm save = atmRepository.save(atm);
        log.info("Банкомат c id {} был успешно обновлен в БД", id);
        return mapper.map(save);
    }


    private Atm findAtmById(Long id) {
        return atmRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Банкомат с указанным id не найден в БД {}", id);
                    return new EntityNotFoundException("Банкомат с указанным id не найден в БД" + id);
                });
    }

    private void setBranchForAtm(Atm atm, Branch branch) {
        if (branch != null) {
            Branch foundBranch = branchRepository.findById(branch.getId())
                    .orElseThrow(() -> {
                        log.error("Не удалось связать отделение бака и банкомат в БД ");
                       return new EntityNotFoundException("Отделение банка по id не найдено в БД" + branch.getId());
                    });
            atm.setBranch(foundBranch);
        }
    }

    private void handleAllHours(Atm atm) {
        if (Boolean.TRUE.equals(atm.getAllHours())) {
            atm.setStartOfWork(null);
            atm.setEndOfWork(null);
            log.info("Банкомат с id: {} работает круглосуточно", atm.getId());
        }
    }

    private void removeAtmFromBranch(Atm atm) {
        Branch branch = atm.getBranch();
        if (branch != null) {
            branch.getAtms().remove(atm);
            log.info("Банкомат с id: {} удален из отделения с id: {}", atm.getId(), branch.getId());
        }
    }
}

