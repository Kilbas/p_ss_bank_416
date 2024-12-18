package com.bank.history.repositories;

import com.bank.history.models.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAll();

    Optional<History> findById(Long id);

    void deleteById(Long id);

}
