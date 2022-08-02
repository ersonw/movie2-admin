package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GameFunds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface GameFundsDao extends JpaRepository<GameFunds, Long>, CrudRepository<GameFunds, Long> {
    Page<GameFunds> findAllByUserId(long userId, Pageable pageable);
    GameFunds findAllById(long id);
}
