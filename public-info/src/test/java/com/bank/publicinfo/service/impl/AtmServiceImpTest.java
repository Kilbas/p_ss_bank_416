package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.entity.Branch;
import com.bank.publicinfo.mapper.AtmMapper;
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
@DisplayName("Класс для тестирование AtmService.")
class AtmServiceImpTest {

    private static final Long ID = 11L;

    @Mock
    private AtmRepository atmRepository;

    @Mock
    private BranchRepository branchRepository;

    private AtmMapper mapper;

    private AtmServiceImp atmService;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AtmMapper.class);
        atmService = new AtmServiceImp(atmRepository, mapper, branchRepository);
    }

    @Test
    @DisplayName("Тест на получение списка Atm.")
    void getAllAtms() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<Atm> all = new PageImpl<>(List.of(CreateAtmAndBranch.createAtm(), CreateAtmAndBranch.createAtm()));
        doReturn(all).when(atmRepository).findAll(pageable);

        List<AtmDTO> result = atmService.getAllAtms(pageable);

        assertFalse(CollectionUtils.isEmpty(result));
        assertEquals(pageable.getPageSize(), result.size());
        verify(atmRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Тест на получение Atm.")
    void getAtm() {
        Atm atm = CreateAtmAndBranch.createAtm();
        doReturn(Optional.of(atm)).when(atmRepository).findById(atm.getId());

        AtmDTO result = atmService.getAtm(atm.getId());

        assertNotNull(result);
        assertEquals(result.getId(), atm.getId());
        verify(atmRepository).findById(atm.getId());
    }

    @Test
    @DisplayName("Тест на получение Atm. Atm не найденю")
    void getAtmNotFound() {
        doReturn(Optional.empty()).when(atmRepository).findById(ID);

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> atmService.getAtm(ID));

        assertEquals(String.format("Банкомат с указанным id не найден в БД %s", ID), entityNotFoundException.getMessage());
        verify(atmRepository).findById(ID);
    }

    @Test
    @DisplayName("Тест на удаление Atm.")
    void deleteAtm() {
        Atm atm = CreateAtmAndBranch.createAtm();
        doReturn(Optional.of(atm)).when(atmRepository).findById(atm.getId());
        doNothing().when(atmRepository).deleteById(atm.getId());

        atmService.deleteAtm(atm.getId());

        verify(atmRepository).findById(atm.getId());
        verify(atmRepository).deleteById(atm.getId());
    }

    @Test
    @DisplayName("Тест на удаление Atm с удалением Branch.")
    void deleteAtmWhenIsDeleteBranch() {
        Atm atm = CreateAtmAndBranch.createAtm();
        atm.setBranch(null);
        doReturn(Optional.of(atm)).when(atmRepository).findById(atm.getId());
        doNothing().when(atmRepository).deleteById(atm.getId());

        atmService.deleteAtm(atm.getId());

        verify(atmRepository).findById(atm.getId());
        verify(atmRepository).deleteById(atm.getId());
    }

    @Test
    @DisplayName("Тест на добавление Atm.")
    void addAtm() {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        Branch branch = atmDTO.getBranch();
        Atm map = mapper.map(atmDTO);
        map.setId(atmDTO.getId());
        doReturn(Optional.of(branch)).when(branchRepository).findById(branch.getId());
        doReturn(map).when(atmRepository).save(any());

        AtmDTO result = atmService.addAtm(atmDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(atmDTO.getAddress(), result.getAddress());
        assertEquals(atmDTO.getStartOfWork(), result.getStartOfWork());
        assertEquals(atmDTO.getEndOfWork(), result.getEndOfWork());
        assertEquals(atmDTO.getBranch(), result.getBranch());
        verify(branchRepository).findById(branch.getId());
        verify(atmRepository).save(any());
    }

    @Test
    @DisplayName("Тест на добавление Atm. Не найден Branch.")
    void addAtmNotFoundBranch() {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        Branch branch = atmDTO.getBranch();
        doReturn(Optional.empty()).when(branchRepository).findById(branch.getId());

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> atmService.addAtm(atmDTO));
        assertEquals(String.format("Не удалось связать банкомат и отделение бака так как отделение с id = %s не найдено в БД", branch.getId()), entityNotFoundException.getMessage());
        verify(branchRepository).findById(branch.getId());
    }

    @Test
    @DisplayName("Тест на изменение Atm.")
    void updateAtm() {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        atmDTO.setAllHours(true);
        atmDTO.setBranch(null);
        Atm atm = CreateAtmAndBranch.createAtm();
        atm.setId(atmDTO.getId());
        atm.setBranch(null);
        mapper.updateAtmFromDto(atmDTO, atm);
        doReturn(Optional.of(atm)).when(atmRepository).findById(atmDTO.getId());
        doReturn(atm).when(atmRepository).save(any());

        AtmDTO result = atmService.updateAtm(atmDTO.getId(), atmDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(atmDTO.getAddress(), result.getAddress());
        assertNull(result.getStartOfWork());
        assertNull(result.getEndOfWork());
        assertNull(result.getBranch());
        verify(atmRepository).save(any());
        verify(atmRepository).findById(atmDTO.getId());
    }

    @Test
    @DisplayName("Тест на изменение Atm с удалением связи Brunch.")
    void updateAtmWhenIsDeleteBranch() {
        AtmDTO atmDTO = CreateAtmAndBranch.createAtmDTO();
        atmDTO.setAllHours(true);
        atmDTO.setBranch(null);
        atmDTO.setIsDeleteBranch(true);
        Atm atm = CreateAtmAndBranch.createAtm();
        atm.setId(atmDTO.getId());
        atm.setBranch(null);
        mapper.updateAtmFromDto(atmDTO, atm);
        doReturn(Optional.of(atm)).when(atmRepository).findById(atmDTO.getId());
        doReturn(atm).when(atmRepository).save(any());

        AtmDTO result = atmService.updateAtm(atmDTO.getId(), atmDTO);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(atmDTO.getAddress(), result.getAddress());
        assertNull(result.getStartOfWork());
        assertNull(result.getEndOfWork());
        assertNull(result.getBranch());
        verify(atmRepository).save(any());
        verify(atmRepository).findById(atmDTO.getId());
    }
}
