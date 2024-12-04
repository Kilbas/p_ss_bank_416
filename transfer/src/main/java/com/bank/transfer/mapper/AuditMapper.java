package com.bank.transfer.mapper;

import com.bank.transfer.dto.AuditDTO;
import com.bank.transfer.model.Audit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {
    AuditDTO auditToAuditDTO(Audit audit);
    Audit auditDTOToAudit(AuditDTO auditDTO);
}
