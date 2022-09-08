package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GameOutConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface GameOutConfigDao extends JpaRepository<GameOutConfig, Long>, CrudRepository<GameOutConfig, Long> {
    GameOutConfig findAllById(long id);
    List<GameOutConfig> findAllByName(String name);

    GameOutConfig findByName(String key);
}
