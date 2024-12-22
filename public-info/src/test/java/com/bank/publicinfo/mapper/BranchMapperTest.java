package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import com.bank.publicinfo.util.CreateAtmAndBranch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@DisplayName("Класс для тестирования BranchMapper")
class BranchMapperTest {
    private BranchMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = Mappers.getMapper(BranchMapper.class);
    }

    private void assertBranchEquals(BranchDTO branchDto, Branch branch) {
        assertEquals(branchDto.getAddress(), branch.getAddress());
        assertEquals(branchDto.getPhoneNumber(), branch.getPhoneNumber());
        assertEquals(branchDto.getCity(), branch.getCity());
        assertEquals(branchDto.getStartOfWork(), branch.getStartOfWork());
        assertEquals(branchDto.getEndOfWork(), branch.getEndOfWork());
        assertEquals(branchDto.getAtms(), branch.getAtms());
    }

    private void assertBranchDTOEquals(Branch branch, BranchDTO result) {
        assertEquals(branch.getId(), result.getId());
        assertEquals(branch.getAddress(), result.getAddress());
        assertEquals(branch.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(branch.getCity(), result.getCity());
        assertEquals(branch.getStartOfWork(), result.getStartOfWork());
        assertEquals(branch.getEndOfWork(), result.getEndOfWork());
        assertEquals(branch.getAtms(), result.getAtms());
    }

    @Test
    @DisplayName("Маппинг из BranchDTO в Branch")
    void mapBranchDTOToBranch() {
        BranchDTO branchDto = CreateAtmAndBranch.createBranchDTO();
        Atm atm = CreateAtmAndBranch.createAtm();
        branchDto.getAtms().add(atm);

        Branch result = mapper.map(branchDto);

        assertBranchEquals(branchDto, result);
        assertNull(result.getId());
    }

    @Test
    @DisplayName("Маппинг из Branch в BranchDTO")
    void mapBranchToBranchDTO() {
        Branch branch = CreateAtmAndBranch.createBranch();
        Atm atm = CreateAtmAndBranch.createAtm();
        branch.getAtms().add(atm);

        BranchDTO result = mapper.map(branch);

        assertBranchDTOEquals(branch, result);
    }

    @Test
    @DisplayName("Маппинг из List<Branch> в List<BranchDTO>")
    void mapAllBranchToAllBranchDTO() {
        Branch branch = CreateAtmAndBranch.createBranch();
        Atm atm = CreateAtmAndBranch.createAtm();
        branch.getAtms().add(atm);
        List<Branch> allBranch = List.of(branch);

        List<BranchDTO> result = mapper.map(allBranch);

        assertEquals(allBranch.size(), result.size());
        assertBranchDTOEquals(branch, result.get(0));
    }

    @Test
    @DisplayName("Маппинг из BranchDTO в Branch при обновлении сущности в БД")
    void mapBranchDTOUpdateToBranch() {
        BranchDTO branchDto = CreateAtmAndBranch.createBranchDTO();
        Atm atm = CreateAtmAndBranch.createAtm();
        branchDto.getAtms().add(atm);
        Branch branch = CreateAtmAndBranch.createBranch();

        mapper.updateBranchFromDto(branchDto, branch);

        assertBranchEquals(branchDto, branch);
        assertNotNull(branch.getId());
    }
}
