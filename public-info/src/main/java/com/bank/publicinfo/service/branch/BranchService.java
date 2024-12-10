package com.bank.publicinfo.service.branch;

import com.bank.publicinfo.dto.BranchDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BranchService {

    List<BranchDTO> getAllBranches(Pageable pageable);

    BranchDTO getBranch(Long id);

    BranchDTO addBranch(BranchDTO branch);

    BranchDTO updateBranch(Long id, BranchDTO updateBranch);

    void deleteBranch(Long id);
}