package com.example.movie2admin.dao;

import com.example.movie2admin.entity.CoinConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Repository
public interface CoinConfigDao extends JpaRepository<CoinConfig, Long>, CrudRepository<CoinConfig, Long> {
    CoinConfig findAllById(long id);
    List<CoinConfig> findAllByName(String name);
}
