package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BranchDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.repository.BranchRepository;
import com.bank.publicinfo.util.CreateAtmAndBranch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
@DisplayName("Класс для тестирование BranchService.")
class BranchServiceImpTest {

    private static final Long ID = 11L;

    @Mock
    private BranchRepository repository;

    @Mock
    private AtmRepository atmRepository;

    private BranchMapper mapper;

    private BranchServiceImp serviceImp;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(BranchMapper.class);
        serviceImp = new BranchServiceImp(repository, mapper, atmRepository);
    }

    @Test
    @DisplayName("Тест на получение списка Branch.")
    void getAllBranch() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<Branch> all = new PageImpl<>(List.of(CreateAtmAndBranch.createBranch(), CreateAtmAndBranch.createBranch()));
        doReturn(all).when(repository).findAll(pageable);

        List<BranchDTO> result = serviceImp.getAllBranches(pageable);

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(pageable.getPageSize(), result.size());
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Тест на получение Branch.")
    void getBranch() {
        Branch expect = CreateAtmAndBranch.createBranch();
        doReturn(Optional.of(expect)).when(repository).findById(expect.getId());

        BranchDTO result = serviceImp.getBranch(expect.getId());

        assertNotNull(result);
        assertEquals(result.getId(), expect.getId());
        verify(repository).findById(expect.getId());
    }

    @Test
    @DisplayName("Тест на получение Branch. Branch не найденю")
    void getBranchNotFound() {
        doReturn(Optional.empty()).when(repository).findById(ID);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> serviceImp.getBranch(ID));

        assertEquals(String.format("Отделение не найден с id %s", ID), entityNotFoundException.getMessage());
        verify(repository).findById(ID);
    }

    @Test
    @DisplayName("Тест на удаление Branch.")
    void deleteBranch() {
        Branch expect = CreateAtmAndBranch.createBranch();
        doReturn(Optional.of(expect)).when(repository).findById(expect.getId());
        doNothing().when(repository).deleteById(expect.getId());

        serviceImp.deleteBranch(expect.getId());

        verify(repository).findById(expect.getId());
        verify(repository).deleteById(expect.getId());
    }

    @Test
    @DisplayName("Тест на добавление Branch.")
    void addBranch() {
        BranchDTO expect = CreateAtmAndBranch.createBranchDTO();
        Atm atm = CreateAtmAndBranch.createAtm();
        expect.getAtms().add(atm);
        Branch map = mapper.map(expect);
        map.setId(expect.getId());
        doReturn(Optional.of(atm)).when(atmRepository).findById(atm.getId());
        doReturn(map).when(repository).save(any());

        BranchDTO result = serviceImp.addBranch(expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(expect.getAddress(), result.getAddress());
        assertEquals(expect.getStartOfWork(), result.getStartOfWork());
        assertEquals(expect.getEndOfWork(), result.getEndOfWork());
        assertEquals(expect.getAtms(), result.getAtms());
        verify(atmRepository).findById(atm.getId());
        verify(repository).save(any());
    }

    @Test
    @DisplayName("Тест на добавление Branch. Не найден Atm.")
    void addBranchNotFoundAtm() {
        BranchDTO expect = CreateAtmAndBranch.createBranchDTO();
        Atm atm = CreateAtmAndBranch.createAtm();
        expect.getAtms().add(atm);
        doReturn(Optional.empty()).when(atmRepository).findById(atm.getId());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> serviceImp.addBranch(expect));

        assertEquals(String.format("Банкомат не найден с id %s", atm.getId()), entityNotFoundException.getMessage());
        verify(atmRepository).findById(atm.getId());
    }

    @Test
    @DisplayName("Тест на изменение Branch.")
    void updateBranch() {
        BranchDTO expect = CreateAtmAndBranch.createBranchDTO();
        Branch branch = CreateAtmAndBranch.createBranch();
        branch.setId(expect.getId());
        mapper.updateBranchFromDto(expect, branch);
        doReturn(Optional.of(branch)).when(repository).findById(expect.getId());
        doReturn(branch).when(repository).save(any());

        BranchDTO result = serviceImp.updateBranch(expect.getId(), expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(expect.getAddress(), result.getAddress());
        assertEquals(expect.getStartOfWork(), result.getStartOfWork());
        assertEquals(expect.getEndOfWork(), result.getEndOfWork());
        verify(repository).save(any());
        verify(repository).findById(expect.getId());
    }

    @Test
    @DisplayName("Тест на изменение Branch. с удалением связи Atm.")
    void updateBranchWhenIsDeleteAtm() {
        BranchDTO expect = CreateAtmAndBranch.createBranchDTO();
        Atm atm = CreateAtmAndBranch.createAtm();
        expect.getAtms().add(atm);
        atm.setBranch(null);
        expect.setIsDeleteAtm(true);
        Branch branch = CreateAtmAndBranch.createBranch();
        branch.setId(expect.getId());
        mapper.updateBranchFromDto(expect, branch);
        doReturn(Optional.of(atm)).when(atmRepository).findById(atm.getId());
        doReturn(Optional.of(branch)).when(repository).findById(expect.getId());
        doReturn(branch).when(repository).save(any());

        BranchDTO result = serviceImp.updateBranch(expect.getId(), expect);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNull(atm.getBranch());
        assertEquals(expect.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(expect.getCity(), result.getCity());
        verify(repository).save(any());
        verify(repository).findById(expect.getId());
    }
}