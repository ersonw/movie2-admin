package com.example.movie2admin.dao;

import com.example.movie2admin.entity.ShortVideoPpvod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ShortVideoPpvodDao extends JpaRepository<ShortVideoPpvod, Long>, CrudRepository<ShortVideoPpvod, Long> {
    List<ShortVideoPpvod> findAllByName(String name);
    ShortVideoPpvod findAllById(long id);
}
