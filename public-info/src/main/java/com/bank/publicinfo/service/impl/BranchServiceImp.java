package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.repository.BranchRepository;
import com.bank.publicinfo.service.interfaceEntity.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BranchServiceImp implements BranchService {

    private String errorMessage;
    private final BranchRepository branchRepository;
    private final BranchMapper mapper;
    private final AtmRepository atmRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BranchDTO> getAllBranches(Pageable pageable) {
        Page<Branch> branches = branchRepository.findAll(pageable);
        return mapper.map(branches.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public BranchDTO getBranch(Long id) {
        Branch branchById = findBranchById(id);
        return mapper.map(branchById);
    }

    @Override
    public void deleteBranch(Long id) {
        Branch branch = findBranchById(id);
        removeBranchFromAtm(branch);
        branchRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    public BranchDTO addBranch(BranchDTO branchDTO) {
        Branch branch = mapper.map(branchDTO);
        setBranchAtms(branchDTO, branch);
        return mapper.map(branchRepository.save(branch));
    }

    @Override
    @AuditAnnotation
    public BranchDTO updateBranch(Long id, BranchDTO updateBranch) {
        Branch branch = findBranchById(id);
        mapper.updateBranchFromDto(updateBranch, branch);
        setBranchAtms(updateBranch, branch);

        if (Boolean.TRUE.equals(updateBranch.getIsDeleteAtm())) {
            removeBranchFromAtm(branch);
        }

        Branch save = branchRepository.save(branch);
        return mapper.map(save);
    }

    private void setBranchAtms(BranchDTO branchCreateDTO, Branch branch) {
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
                removeBranchFromAtm(branch);
            }
        }
    }

    private <T> T findEntityById(Long id, Function<Long, Optional<T>> findFunction, String entityName) {
        return findFunction.apply(id)
                .orElseThrow(() -> {
                    errorMessage = String.format("%s не найден с id %s", entityName, id);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    private Branch findBranchById(Long id) {
        return findEntityById(id, branchRepository::findById, "Отделение");
    }

    private Atm findAtmById(Long id) {
        return findEntityById(id, atmRepository::findById, "Банкомат");
    }

    private void removeBranchFromAtm(Branch branch) {
        for (Atm atm : branch.getAtms()) {
            atm.setBranch(null);
        }
        branch.getAtms().clear();
    }
}