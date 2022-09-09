package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GamePublicity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface GamePublicityDao  extends JpaRepository<GamePublicity, Long>, CrudRepository<GamePublicity, Long> {
    List<GamePublicity> findAllByStatus(int status);
    GamePublicity findAllById(long id);

    Page<GamePublicity> findAllByNameLike(String s, Pageable pageable);
}
