package com.bank.publicinfo.service.branch;

import com.bank.publicinfo.dto.BranchDTO;

import java.util.List;

public interface BranchService {

    List<BranchDTO> getAllBranches();

    BranchDTO getBranch(Long id);

    BranchDTO addBranch(BranchDTO branch);

    BranchDTO updateBranch(Long id, BranchDTO updateBranch);

    BranchDTO deleteBranch(Long id);
}
