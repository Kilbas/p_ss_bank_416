package com.bank.transfer.service;

import com.bank.transfer.model.Audit;
import com.bank.transfer.repository.AuditRepository;
import com.bank.transfer.serviceImpl.AuditServiceImpl;
import com.bank.transfer.util.TestAuditConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class TransferAuditServiceTest {
    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    private Audit audit;

    @BeforeEach
    void setUp() {
        audit = new Audit(
                TestAuditConstants.ENTITY_TYPE,
                TestAuditConstants.OPERATION_TYPE,
                TestAuditConstants.CREATED_BY,
                TestAuditConstants.MODIFY_BY,
                LocalDateTime.now(),
                LocalDateTime.now(),
                TestAuditConstants.NEW_ENTITY_JSON,
                TestAuditConstants.ENTITY_JSON
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
        when(auditRepository.findByEntityTypeAndEntityId(TestAuditConstants.ENTITY_TYPE, TestAuditConstants.ENTITY_ID)).thenReturn(audit);

        Audit result = auditService.findByEntityTypeAndEntityId(TestAuditConstants.ENTITY_TYPE, TestAuditConstants.ENTITY_ID);

        assertNotNull(result);
        assertEquals(audit.getId(), result.getId());
        assertEquals(audit.getEntityType(), result.getEntityType());
        verify(auditRepository, times(2))
                .findByEntityTypeAndEntityId(TestAuditConstants.ENTITY_TYPE, TestAuditConstants.ENTITY_ID);
    }

    @Test
    @DisplayName("findByEntityTypeAndEntityId поиск Audit по EntityId и EntityType выбрасывает EntityNotFoundException")
    void findByEntityTypeAndEntityId_ShouldThrowException_WhenAuditNotFound() {
        when(auditRepository
                .findByEntityTypeAndEntityId(TestAuditConstants.NON_EXISTENT_ENTITY_TYPE, TestAuditConstants.NON_EXISTENT_ENTITY_ID))
                .thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> auditService.findByEntityTypeAndEntityId(TestAuditConstants.NON_EXISTENT_ENTITY_TYPE, TestAuditConstants.NON_EXISTENT_ENTITY_ID));

        assertEquals(String.format(TestAuditConstants.ENTITY_NOT_FOUND_MESSAGE,
                TestAuditConstants.NON_EXISTENT_ENTITY_TYPE, TestAuditConstants.NON_EXISTENT_ENTITY_ID), exception.getMessage());
        verify(auditRepository, times(1))
                .findByEntityTypeAndEntityId(TestAuditConstants.NON_EXISTENT_ENTITY_TYPE, TestAuditConstants.NON_EXISTENT_ENTITY_ID);
    }
}
