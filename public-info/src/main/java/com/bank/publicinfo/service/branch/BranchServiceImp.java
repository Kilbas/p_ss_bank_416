package com.bank.publicinfo.service.branch;


import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.repository.BranchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class BranchServiceImp implements BranchService {

    private final BranchRepository branchRepository;
    private final BranchMapper mapper;
    private final AtmRepository atmRepository;

    public BranchServiceImp(BranchRepository branchRepository, BranchMapper mapper, AtmRepository atmRepository) {
        this.branchRepository = branchRepository;
        this.mapper = mapper;
        this.atmRepository = atmRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> getAllBranches() {
        log.info("Запрос на получение всех отделений");
        List<Branch> branches = branchRepository.findAll();
        log.info("Найдено {} отделений", branches.size());
        return mapper.map(branches);
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO getBranch(Long id) {
        log.info("Запрос на получение отделения с id {}", id);
        Branch branchById = findBranchById(id);
        log.info("Получено отделение с id : {}", id);
        return mapper.map(branchById);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public BranchDTO deleteBranch(Long id) {
        log.info("Запрос на удаление отделения с id {}", id);
        Branch branch = findBranchById(id);
        removeBranchFromAtm(branch);
        branchRepository.deleteById(id);
        log.info("Отделение с id {} успешно удалено", id);
        return mapper.map(branch);
    }

    @Override
    @AuditAnnotation
    @Transactional
    public BranchDTO addBranch(BranchDTO branchDTO) {
        Branch branch = mapper.map(branchDTO);
        setBranchAtms(branchDTO, branch);
        return mapper.map(branchRepository.save(branch));
    }

    @Override
    @AuditAnnotation
    @Transactional
    public BranchDTO updateBranch(Long id, BranchDTO updateBranch) {
        log.info("Запрос на обновление отделения с id {}", id);
        Branch branch = findBranchById(id);
        mapper.updateBranchFromDto(updateBranch, branch);
        setBranchAtms(updateBranch, branch);

        if (Boolean.TRUE.equals(updateBranch.getIsDeleteAtm())) {
            log.info("Удаление банкоматов из отделения с id {}", id);
            removeBranchFromAtm(branch);
        }

        Branch save = branchRepository.save(branch);
        log.info("Отделение обновлено с id {}", id);
        return mapper.map(save);
    }

    private void setBranchAtms(BranchDTO branchCreateDTO, Branch branch) {
        log.info("Создание связи отделения с банкоматами для отделения с id {}", branch.getId());
        Set<Atm> currentAtms = branchCreateDTO.getAtms();
        Set<Atm> foundAtms = new HashSet<>();

        if (!CollectionUtils.isEmpty(currentAtms)) {
            for (Atm atm : currentAtms) {
                Atm foundAtm = findAtmById(atm.getId());
                foundAtms.add(foundAtm);
                foundAtm.setBranch(branch);
            }
            branch.setAtms(foundAtms);
        } else {
            if (Boolean.TRUE.equals(branchCreateDTO.getIsDeleteAtm())) {
                log.info("Удаление банкоматов из отделения");
                removeBranchFromAtm(branch);
            }
        }
    }

    private Branch findBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Отделение не найдено с id {}", id);
                    return new EntityNotFoundException("Отделение банка не найдено");
                });
    }

    private Atm findAtmById(Long id) {
        return atmRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Банкомат не найден с id {}", id);
                    return new EntityNotFoundException("Банкомат не найден");
                });
    }

    private void removeBranchFromAtm(Branch branch) {
        log.info("Удаление связи отделения с банкоматами для отделения с id {}", branch.getId());
        for (Atm atm : branch.getAtms()) {
            atm.setBranch(null);
        }
        branch.getAtms().clear();
        log.info("Удалена связь с  банкоматами");
    }
}
