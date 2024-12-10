package com.bank.publicinfo.mapper;


import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.entity.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    @Mapping(target = "id", source = "id", ignore = true)
    Branch map(BranchDTO branchCreateDTO);

    BranchDTO map(Branch branch);

    List<BranchDTO> map(List<Branch> allBranch);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "address", source = "address",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phoneNumber", source = "phoneNumber",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "city", source = "city",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "startOfWork", source = "startOfWork",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "endOfWork", source = "endOfWork",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "atms", source = "atms",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBranchFromDto(BranchDTO dto, @MappingTarget Branch branch);
}