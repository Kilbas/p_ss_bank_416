//package com.bank.antifraud.aspect;
//
//import com.bank.antifraud.entity.Audit;
//import com.bank.antifraud.service.AuditService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Aspect
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class AuditAspect {
//
//
//    private final AuditService auditService;
//
//    @AfterReturning(value = "execution(* com.bank.antifraud.service.*.create*(..))", returning = "result")
//    public void afterCreateOperation(JoinPoint joinPoint, Object result) {
//        Audit audit = new Audit();
//        audit.setEntityType(result.getClass().getSimpleName());
//        audit.setOperationType(joinPoint.getSignature().getName());
//        audit.setCreatedBy("SYSTEM");
//        audit.setCreatedAt(LocalDateTime.now());
//        audit.setEntityJson(result.toString());
//
//        auditService.addAudit(audit);
//        log.info("Создана запись аудита для метода create: {}", audit);
//    }
//
//    @AfterReturning(value = "execution(* com.bank.antifraud.service.*.update*(..))", returning = "result")
//    public void afterUpdateOperation(JoinPoint joinPoint, Object result) {
//        Object[] args = joinPoint.getArgs();
//        Long id = (Long) args[0]; // Предполагаем, что первый аргумент - это ID сущности.
//
//        Audit audit = new Audit();
//        audit.setEntityType(result.getClass().getSimpleName());
//        audit.setOperationType("UPDATE");
//        audit.setModifiedBy("SYSTEM");
//        audit.setModifiedAt(LocalDateTime.now());
//        audit.setNewEntityJson(result.toString());
//
//        auditService.addAudit(audit);
//        log.info("Создана запись аудита для метода update: {}", audit);
//    }
//
//    @AfterReturning("execution(* com.bank.antifraud.service.*.delete*(..))")
//    public void afterDeleteOperation(JoinPoint joinPoint) {
//        Object[] args = joinPoint.getArgs();
//        Long id = (Long) args[0]; // ID удаляемой записи
//
//        Audit audit = new Audit();
//        audit.setEntityType("Unknown"); // Тип можно уточнить, если у вас есть детали
//        audit.setOperationType("DELETE");
//        audit.setCreatedBy("SYSTEM");
//        audit.setCreatedAt(LocalDateTime.now());
//        audit.setEntityJson(String.format("Запись с ID %d была удалена", id));
//
//        auditService.addAudit(audit);
//        log.info("Создана запись аудита для метода delete: {}", audit);
//    }
//
//}
