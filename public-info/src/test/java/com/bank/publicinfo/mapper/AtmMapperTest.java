package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AtmDTO;
import com.bank.publicinfo.entity.Atm;
import com.bank.publicinfo.util.CreateAtmAndBranch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Класс для тестирования AtmMapper")
class AtmMapperTest {
    private AtmMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = Mappers.getMapper(AtmMapper.class);
    }

    private void assertAtmEquals(AtmDTO atmDto, Atm atm) {
        assertEquals(atmDto.getAddress(), atm.getAddress());
        assertEquals(atmDto.getStartOfWork(), atm.getStartOfWork());
        assertEquals(atmDto.getEndOfWork(), atm.getEndOfWork());
        assertEquals(atmDto.getAllHours(), atm.getAllHours());
        assertEquals(atmDto.getBranch(), atm.getBranch());
        assertEquals(atmDto.getBranch().getAtms().size(), atm.getBranch().getAtms().size());
    }

    private void assertAtmDTOEquals(Atm atm, AtmDTO result) {
        assertEquals(atm.getId(), result.getId());
        assertEquals(atm.getAddress(), result.getAddress());
        assertEquals(atm.getStartOfWork(), result.getStartOfWork());
        assertEquals(atm.getEndOfWork(), result.getEndOfWork());
        assertEquals(atm.getAllHours(), result.getAllHours());
        assertEquals(atm.getBranch(), result.getBranch());
        assertEquals(atm.getBranch().getAtms().size(), result.getBranch().getAtms().size());
    }

    @Test
    @DisplayName("Маппинг из AtmDTO в Atm")
    void mapAtmDTOToAtm() {
        AtmDTO atmDto = CreateAtmAndBranch.createAtmDTO();
        Atm atm = CreateAtmAndBranch.createAtm();
        atmDto.getBranch().getAtms().add(atm);

        Atm result = mapper.map(atmDto);

        assertAtmEquals(atmDto, result);
        assertNull(result.getId());
    }

    @Test
    @DisplayName("Маппинг из Atm в AtmDTO")
    void mapAtmToAtmDTO() {
        Atm atm = CreateAtmAndBranch.createAtm();
        atm.getBranch().getAtms().add(atm);

        AtmDTO result = mapper.map(atm);

        assertAtmDTOEquals(atm, result);
    }

    @Test
    @DisplayName("Маппинг из List<Atm> в List<AtmDTO>")
    void mapAllAtmToAllAtmDTO() {
        Atm atm = CreateAtmAndBranch.createAtm();
        List<Atm> allAtm = List.of(atm);

        List<AtmDTO> result = mapper.map(allAtm);

        assertEquals(allAtm.size(), result.size());
        assertAtmDTOEquals(atm, result.get(0));
    }

    @Test
    @DisplayName("Маппинг из AtmDTO в Atm при обновлении сущности в БД")
    void mapAtmDTOUpdateToAtm() {
        AtmDTO atmDto = CreateAtmAndBranch.createAtmDTO();
        Atm atm = CreateAtmAndBranch.createAtm();
        atmDto.getBranch().getAtms().add(atm);

        mapper.updateAtmFromDto(atmDto, atm);

        assertAtmEquals(atmDto, atm);
        assertNotNull(atm.getId());
    }
}
