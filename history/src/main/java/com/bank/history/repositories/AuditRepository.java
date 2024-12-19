package com.bank.history.repositories;

import com.bank.history.models.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {

    List<Audit> findAll();

    /**
     * Кастомный метод лежит здесь -> {@link com.bank.history.config.PostgresCustom}
     */

    @Query("from Audit a where a.operation = 'CREATE' and jsonExtract(a.newEntityJson, 'id') = :entityId ")
    Optional<Audit> findByEntityId(@Param("entityId") String id);
}
