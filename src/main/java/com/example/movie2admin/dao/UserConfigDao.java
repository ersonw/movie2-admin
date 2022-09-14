package com.example.movie2admin.dao;

import com.example.movie2admin.entity.UserConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserConfigDao extends JpaRepository<UserConfig, Long>, CrudRepository<UserConfig, Long> {

    UserConfig findAllById(long id);

    List<UserConfig> findAllByName(String name);

    UserConfig findByName(String key);
}
