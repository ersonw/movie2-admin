package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GameScroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface GameScrollDao extends JpaRepository<GameScroll, Long>, CrudRepository<GameScroll, Long> {
    Page<GameScroll> findAllByAddTimeGreaterThanEqual(long time, Pageable pageable);
    GameScroll findAllById(long id);
    Page<GameScroll> findAllByNameLike(String s, Pageable pageable);
}
