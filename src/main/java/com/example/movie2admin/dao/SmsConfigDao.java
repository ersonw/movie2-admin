package com.example.movie2admin.dao;

import com.example.movie2admin.entity.SmsConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface SmsConfigDao extends JpaRepository<SmsConfig, Long>, CrudRepository<SmsConfig, Long> {
    SmsConfig findAllById(long id);
    List<SmsConfig> findAllByName(String name);
}
