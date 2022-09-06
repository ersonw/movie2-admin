package com.example.movie2admin.dao;

import com.example.movie2admin.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface GameDao extends JpaRepository<Game, Long>, CrudRepository<Game, Long> {
    Game findAllByGameId(Integer integer);
    Game findAllById(long id);
    List<Game> findAllByStatus(int status);

    Page<Game> findAllByName(String name, Pageable pageable);
}
