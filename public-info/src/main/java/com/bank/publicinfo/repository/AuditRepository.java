package com.bank.publicinfo.repository;

import com.bank.publicinfo.entity.Audit;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuditRepository extends JpaRepository<Audit, Long> {

    @Query(value = "SELECT * FROM public_bank_information.audit WHERE entity_type = :entityType AND entity_json like concat('%id\":', :entityId, '%') LIMIT 1", nativeQuery = true)
    Audit findByEntityTypeAndEntityId(@Param("entityType") String entityType, @Param("entityId") String entityId);
}