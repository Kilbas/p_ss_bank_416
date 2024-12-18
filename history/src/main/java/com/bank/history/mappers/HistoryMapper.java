package com.bank.history.mappers;

import com.bank.history.dto.HistoryDTO;
import com.bank.history.models.History;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    HistoryDTO toDTO(History history);

    @Mapping(target = "id", ignore = true)
    History toEntitySave(HistoryDTO historyDTO);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toEntityUpdate(HistoryDTO historyDTO, @MappingTarget History history);

    List<HistoryDTO> listToDTO(List<History> historyList);

}
