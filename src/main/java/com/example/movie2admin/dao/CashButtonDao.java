package com.example.movie2admin.dao;

import com.example.movie2admin.entity.CashButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public interface CashButtonDao extends JpaRepository<CashButton, Long>, CrudRepository<CashButton, Long> {
    CashButton findAllById(Long id);
    @Query(value = "SELECT * FROM `cash_button` WHERE status=1 ORDER BY amount ASC ",nativeQuery = true)
    List<CashButton> getAllButtons();
}
