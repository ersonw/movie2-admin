package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoPpvod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface VideoPpvodDao extends JpaRepository<VideoPpvod, Long>, CrudRepository<VideoPpvod, Long> {
    List<VideoPpvod> findAllByName(String name);
}
