package com.bank.account.service;

import com.bank.account.entity.Audit;
import com.bank.account.repository.AuditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@Nested
@DisplayName("Тесты для класса AuditServiceImp")
@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository auditRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    @BeforeEach
    public void setUp() {

    }

    @Nested
    @DisplayName("Тесты для метода newAudit")
    class NewAuditTests {
        @DisplayName("newAudit успешное сохранение Audit")
        @Test
        public void testNewAuditPositiveSave() {

            Audit audit = new Audit();
            when(auditRepository.save(audit)).thenReturn(audit);

            assertDoesNotThrow(() -> auditService.newAudit(audit));

            verify(auditRepository, times(1)).save(audit);
        }

        @DisplayName("newAudit выбрасывает IllegalArgumentException при Audit: null")
        @Test
        public void testNewAuditNegativeAuditNull() {

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> auditService.newAudit(null));

            assertEquals("Объект Audit не может быть null", exception.getMessage());
        }

        @DisplayName("newAudit выбрасывает DataIntegrityViolationException при не удачном сохранении")
        @Test
        public void testNewAuditNegativeDataIntegrityViolationException() {

            Audit audit = new Audit();
            when(auditRepository.save(audit)).thenThrow(new DataIntegrityViolationException("Database error"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> auditService.newAudit(audit));

            assertAll(
                    () -> assertEquals("Ошибка при сохранении аудита", exception.getMessage()),
                    () -> assertInstanceOf(DataIntegrityViolationException.class, exception.getCause())
            );
        }
    }

    @Nested
    @DisplayName("Тесты для метода FindByEntityTypeAndEntityId")
    class FindByEntityTypeAndEntityIdTest {
        @DisplayName("findByEntityTypeAndEntityId успешно находит аудит")
        @Test
        public void testFindByEntityTypeAndEntityIdPositiveFind() {

            String entityType = "Account";
            Long entityId = 1L;
            Audit audit = new Audit();
            when(auditRepository.findByEntityTypeAndEntityId(entityType, entityId.toString())).thenReturn(audit);

            Audit result = auditService.findByEntityTypeAndEntityId(entityType, entityId);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(audit, result),
                    () -> verify(auditRepository, times(1)).findByEntityTypeAndEntityId(entityType, entityId.toString())
            );
        }

        @DisplayName("findByEntityTypeAndEntityId выбрасывает IllegalArgumentException при null entityType или entityId")
        @ParameterizedTest(name = "entityType = {0}")
        @NullSource
        @ValueSource(strings = {"AccountDetails"})
        public void testFindByEntityTypeAndEntityIdNegativeNullInput(String entityType) {

            Long entityId = calculateEntityId(entityType);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> auditService.findByEntityTypeAndEntityId(entityType, entityId));

            assertEquals("entityType и или entityId не могут быть null", exception.getMessage());
        }

        @DisplayName("findByEntityTypeAndEntityId выбрасывает RuntimeException, если аудит не найден")
        @Test
        public void testFindByEntityTypeAndEntityIdNegativeAuditNotFound() {

            String entityType = "Account";
            Long entityId = 1L;
            when(auditRepository.findByEntityTypeAndEntityId(entityType, entityId.toString())).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> auditService.findByEntityTypeAndEntityId(entityType, entityId));

            assertEquals("Аудит не найден для entityType = Account и entityId = 1", exception.getMessage());
        }

        @DisplayName("findByEntityTypeAndEntityId выбрасывает RuntimeException при DataAccessException")
        @Test
        public void testFindByEntityTypeAndEntityIdNegativeDataAccessException() {

            String entityType = "Account";
            Long entityId = 1L;
            when(auditRepository.findByEntityTypeAndEntityId(entityType, entityId.toString()))
                    .thenThrow(new DataAccessException("Database error") {});

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> auditService.findByEntityTypeAndEntityId(entityType, entityId));

            assertAll(
                    () -> assertEquals("Ошибка при поиске аудита", exception.getMessage()),
                    () -> assertInstanceOf(DataAccessException.class, exception.getCause())
            );
        }

        private Long calculateEntityId(String entityType) {
            return entityType == null ? 1L : null;
        }
    }
}
