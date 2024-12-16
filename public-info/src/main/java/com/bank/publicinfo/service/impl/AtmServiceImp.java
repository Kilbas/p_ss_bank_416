package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.aspect.AuditAnnotation;
import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.repository.BranchRepository;
import com.bank.publicinfo.service.interfaceEntity.AtmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AtmServiceImp implements AtmService {

    private String errorMessage;
    private final AtmRepository atmRepository;
    private final AtmMapper mapper;
    private final BranchRepository branchRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AtmDTO> getAllAtms(Pageable pageable) {
        Page<Atm> all = atmRepository.findAll(pageable);
        return mapper.map(all.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public AtmDTO getAtm(Long id) {
        Atm atmById = findAtmById(id);
        return mapper.map(atmById);

    }

    @Override
    public void deleteAtm(Long id) {
        Atm atm = findAtmById(id);
        removeAtmFromBranch(atm);
        atmRepository.deleteById(id);
    }

    @Override
    @AuditAnnotation
    public AtmDTO addAtm(AtmDTO atmDTO) {
        Atm atm = mapper.map(atmDTO);
        setBranchForAtm(atm, atmDTO.getBranch());
        handleAllHours(atm);
        Atm save = atmRepository.save(atm);
        return mapper.map(save);
    }

    @Override
    @AuditAnnotation
    public AtmDTO updateAtm(Long id, AtmDTO atmDTO) {
        Atm atm = findAtmById(id);
        mapper.updateAtmFromDto(atmDTO, atm);
        setBranchForAtm(atm, atmDTO.getBranch());
        handleAllHours(atm);

        if (Boolean.TRUE.equals(atmDTO.getIsDeleteBranch())) {
            atm.setBranch(null);
        }
        Atm save = atmRepository.save(atm);
        return mapper.map(save);
    }

    private Atm findAtmById(Long id) {
        return atmRepository.findById(id)
                .orElseThrow(() -> {
                    errorMessage = String.format("Банкомат с указанным id не найден в БД %s", id);
                    log.error(errorMessage);
                    return new EntityNotFoundException(errorMessage);
                });
    }

    private void setBranchForAtm(Atm atm, Branch branch) {
        if (branch != null) {
            Branch foundBranch = branchRepository.findById(branch.getId())
                    .orElseThrow(() -> {
                        errorMessage = String.format(
                                "Не удалось связать банкомат и отделение бака так как отделение с id = %s не найдено в БД",
                                branch.getId());
                        log.error(errorMessage);
                        return new EntityNotFoundException(errorMessage);
                    });
            atm.setBranch(foundBranch);
        }
    }

    private void handleAllHours(Atm atm) {
        if (Boolean.TRUE.equals(atm.getAllHours())) {
            atm.setStartOfWork(null);
            atm.setEndOfWork(null);
        }
    }

    private void removeAtmFromBranch(Atm atm) {
        Branch branch = atm.getBranch();
        if (branch != null) {
            branch.getAtms().remove(atm);
        }
    }
}