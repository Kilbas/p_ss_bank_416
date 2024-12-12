package com.bank.transfer.repository;

import com.bank.transfer.model.Audit;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
    @Query(value = "SELECT * FROM transfer.audit WHERE entity_type = :entityType " +
            "AND CAST(entity_json AS jsonb) ->> 'id' = :entityId LIMIT 1", nativeQuery = true)
    Audit findByEntityTypeAndEntityId(@Param("entityType") String entityType,@Param("entityId") String entityId);
}
