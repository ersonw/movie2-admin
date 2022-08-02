package com.example.movie2admin.dao;

import com.example.movie2admin.entity.CashOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface CashOrderDao extends JpaRepository<CashOrder, Long>, CrudRepository<CashOrder, Long> {
}
