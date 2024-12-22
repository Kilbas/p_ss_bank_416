package com.bank.transfer.service;

import com.bank.transfer.model.Audit;
import com.bank.transfer.repository.AuditRepository;
import com.bank.transfer.serviceImpl.AuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferAuditServiceTest {
    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    private Audit audit;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        audit = new Audit(
                "entityType",
                "operationType",
                "createdBy",
                "modifyBy",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "NewEntityJson",
                "entityJson"
        );

        audit.setId(123L);
    }

    @Test
    @DisplayName("addAudit Успешное добавление Audit")
    void addAudit_ShouldSaveAudit() {
        when(auditRepository.save(audit)).thenReturn(audit);

        auditService.addAudit(audit);

        verify(auditRepository, times(1)).save(audit);
    }

    @Test
    @DisplayName("findByEntityTypeAndEntityId успешный Поиск Audit по EntityId и EntityType")
    void findByEntityTypeAndEntityId_ShouldReturnAudit_WhenAuditExists() {
        when(auditRepository.findByEntityTypeAndEntityId("entityType", "123")).thenReturn(audit);

        Audit result = auditService.findByEntityTypeAndEntityId("entityType", "123");

        assertNotNull(result);
        assertEquals(audit.getId(), result.getId());
        assertEquals(audit.getEntityType(), result.getEntityType());
        verify(auditRepository, times(2)).findByEntityTypeAndEntityId("entityType", "123");
    }

    @Test
    @DisplayName("findByEntityTypeAndEntityId поиск Audit по EntityId и EntityType выбрасывает EntityNotFoundException")
    void findByEntityTypeAndEntityId_ShouldThrowException_WhenAuditNotFound() {
        when(auditRepository.findByEntityTypeAndEntityId("type", "1234")).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> auditService.findByEntityTypeAndEntityId("type", "1234"));

        assertEquals("не найден аудит с типом сущности type и айди сущности 1234", exception.getMessage());
        verify(auditRepository, times(1)).findByEntityTypeAndEntityId("type", "1234");
    }
}
