package com.example.movie2admin.dao;

import com.example.movie2admin.entity.CashInOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface CashInOptionDao extends JpaRepository<CashInOption, Long>, CrudRepository<CashInOption, Long> {
    List<CashInOption> findAllByStatusAndCode(int status, String code);
    List<CashInOption> findAllByStatusAndName(int status, String name);
    List<CashInOption> findAllByStatus(int status);
    CashInOption findAllById(Long id);

    CashInOption findAllByName(String s);

    Page<CashInOption> findAllByNameLike(String s, Pageable pageable);

    CashInOption findAllByCode(String code);
}
