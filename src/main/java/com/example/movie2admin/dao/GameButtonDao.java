package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GameButton;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface GameButtonDao extends JpaRepository<GameButton, Long>, CrudRepository<GameButton, Long> {
    GameButton findAllById(Long id);
    @Query(value = "SELECT * FROM `game_button` WHERE status=1 ORDER BY amount ASC ",nativeQuery = true)
    List<GameButton> getAllButtons();

    Page<GameButton> findAllByAmountGreaterThanEqual(long title, Pageable pageable);
}
