package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.repository.AuditRepository;
import com.bank.publicinfo.util.CreateAudit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
@DisplayName("Класс для тестирование AuditService.")
class AuditServiceImpTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditServiceImp auditService;

    @Test
    @DisplayName("Тест на получение Audit.")
    void findByEntityTypeAndEntityIdSuccess() {
        Audit audit = CreateAudit.createAuditAdd();
        doReturn(Optional.of(audit)).when(auditRepository).findByEntityTypeAndEntityId(audit.getEntityType(), String.valueOf(audit.getId()));

        Audit result = auditService.findByEntityTypeAndEntityId(audit.getEntityType(), String.valueOf(audit.getId()));

        assertNotNull(result);
        assertEquals(audit, result);
        verify(auditRepository).findByEntityTypeAndEntityId(audit.getEntityType(), String.valueOf(audit.getId()));
    }

    @Test
    @DisplayName("Тест на получение Audit. Переданы не вверные аргументы.")
    void findByEntityTypeAndEntityIdWhenIllegalArgumentException() {
        Audit audit = CreateAudit.createAuditAdd();
        audit.setEntityType(null);

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> this.auditService.findByEntityTypeAndEntityId(audit.getEntityType(), String.valueOf(audit.getId())));

        assertEquals("Передаваемые параметры entityType и или entityId не могут быть null", illegalArgumentException.getMessage());
    }

    @Test
    @DisplayName("Тест на получение Audit. Сущность не найдена.")
    void findByEntityTypeAndEntityIdWhenEntityNotFoundException() {
        Audit audit = CreateAudit.createAuditAdd();
        doReturn(Optional.empty()).when(auditRepository).findByEntityTypeAndEntityId(audit.getEntityType(),
                String.valueOf(audit.getId()));

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> auditService.findByEntityTypeAndEntityId(audit.getEntityType(), String.valueOf(audit.getId())));

        assertEquals(String.format("Не найдена запись аудита с типом сущности %s и айди сущности %s",
                audit.getEntityType(), audit.getId()), entityNotFoundException.getMessage());
        verify(auditRepository).findByEntityTypeAndEntityId(audit.getEntityType(), String.valueOf(audit.getId()));
    }

    @Test
    @DisplayName("Тест на добавленее Audit.")
    void addAudit() {
        Audit audit = CreateAudit.createAuditAdd();
        doReturn(audit).when(auditRepository).save(audit);

        auditService.addAudit(audit);

        verify(auditRepository).save(audit);
    }

    @Test
    @DisplayName("Тест на добавленее Audit. DataAccessException.")
    void addAuditWhenDataAccessException() {
        Audit audit = CreateAudit.createAuditAdd();
        doThrow(new DuplicateKeyException("text")).when(auditRepository).save(audit);

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> auditService.addAudit(audit));

        assertEquals("Ошибка при сохранении аудита: text", runtimeException.getMessage());
        verify(auditRepository).save(audit);
    }
}
