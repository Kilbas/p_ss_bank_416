package com.bank.publicinfo.aspect;

import com.bank.publicinfo.dto.BankDetailsDTO;
import com.bank.publicinfo.entity.Audit;
import com.bank.publicinfo.service.interfaceEntity.AuditService;
import com.bank.publicinfo.util.CreateAudit;
import com.bank.publicinfo.util.CreateBankDetailsAndLicenseAndCertificate;
import com.bank.publicinfo.util.JoinPointMock;
import com.bank.publicinfo.util.enums.EntityTypeEnum;
import com.bank.publicinfo.util.enums.OperationTypeEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
@DisplayName("Класс для тестирование AuditAspect.")
class AuditAspectTest {
    private static final String NPE_MESSAGE = "Передаваемые параметры JoinPoint и или Auditable не могут быть null";
    private static final String EXP_MESSAGE = "Ожидаемый первый аргумент должен быть типа Long";

    @Mock
    private AuditService auditService;

    private ObjectMapper mapper;

    private AuditAspect aspect;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        aspect = new AuditAspect(mapper, auditService);
    }

    @Test
    @DisplayName("Проверка аудита при создании сущности. Успех")
    void addAuditSuccess() throws JsonProcessingException {
        BankDetailsDTO result = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        JoinPoint jp = JoinPointMock.createJoinPointMock(EntityTypeEnum.BANK_DETAILS, OperationTypeEnum.CREATE, result.getId());
        doNothing().when(auditService).addAudit(any());

        aspect.afterReturningAudit(jp, result);

        verify(auditService, times(1)).addAudit(any());
    }

    @Test
    @DisplayName("Проверка аудита при изменении сущности. Успех")
    void updateAuditSuccess() throws JsonProcessingException {
        BankDetailsDTO result = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        Audit audit = CreateAudit.createAuditAdd();
        audit.setNewEntityJson(mapper.writeValueAsString(result));
        JoinPoint jp = JoinPointMock.createJoinPointMock(EntityTypeEnum.BANK_DETAILS, OperationTypeEnum.UPDATE, result.getId());
        doReturn(audit).when(auditService).findByEntityTypeAndEntityId(EntityTypeEnum.BANK_DETAILS.getValue(), String.valueOf(jp.getArgs()[0]));
        doNothing().when(auditService).addAudit(any());

        aspect.afterReturningAudit(jp, result);

        verify(auditService, times(1)).addAudit(any());
        verify(auditService, times(1)).findByEntityTypeAndEntityId(EntityTypeEnum.BANK_DETAILS.getValue(),
                String.valueOf(jp.getArgs()[0]));
    }

    @Test
    @DisplayName("Проверка аудита при изменении сущности. Невалидные параметры метода")
    void updateAuditWhenArgsNotValid() {
        BankDetailsDTO result = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        JoinPoint jp1 = JoinPointMock.createJoinPointMock(EntityTypeEnum.BANK_DETAILS, OperationTypeEnum.UPDATE, -1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> aspect.afterReturningAudit(jp1, result));

        assertNotNull(exception);
        assertEquals(EXP_MESSAGE, exception.getMessage());

        JoinPoint jp2 = JoinPointMock.createJoinPointMock(EntityTypeEnum.BANK_DETAILS, OperationTypeEnum.UPDATE, -2L);

        exception = assertThrows(IllegalArgumentException.class, () -> aspect.afterReturningAudit(jp2, result));

        assertNotNull(exception);
        assertEquals(EXP_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Проверка аудита. Неизвестный тип сущности")
    void auditWhenUnsupportedEntity() {
        BankDetailsDTO result = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        JoinPoint jp = JoinPointMock.createJoinPointMock(EntityTypeEnum.UNSUPPORTED_ENTITY, OperationTypeEnum.CREATE, result.getId());

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> aspect.afterReturningAudit(jp, result));

        assertEquals(String.format("Не удалось определить тип сущности при попытке записать в Audit: %s",
                jp.getSignature().getName()), exception.getMessage());
    }

    @Test
    @DisplayName("Проверка аудита. Неизвестный тип операции")
    void auditWhenUnsupportedOperation() {
        BankDetailsDTO result = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        JoinPoint jp = JoinPointMock.createJoinPointMock(EntityTypeEnum.BANK_DETAILS, OperationTypeEnum.UNSUPPORTED_OPERATION, result.getId());

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> aspect.afterReturningAudit(jp, result));

        assertEquals(String.format("Не удалось определить тип операции над сущностью при попытке записать в Audit: %s",
                jp.getSignature().getName()), exception.getMessage());
    }

    @Test
    @DisplayName("Проверка аудита когда Jp или result null")
    void auditWhenJpOrResultNull() {
        BankDetailsDTO result = CreateBankDetailsAndLicenseAndCertificate.createBankDetailsDTO();
        JoinPoint jp = JoinPointMock.createJoinPointMock(EntityTypeEnum.BANK_DETAILS, OperationTypeEnum.CREATE, result.getId());

        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> aspect.afterReturningAudit(null, result));

        assertEquals(NPE_MESSAGE, exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> aspect.afterReturningAudit(jp, null));

        assertEquals(NPE_MESSAGE, exception.getMessage());

        exception = assertThrows(NullPointerException.class, () -> aspect.afterReturningAudit(null, null));

        assertEquals(NPE_MESSAGE, exception.getMessage());
    }
}