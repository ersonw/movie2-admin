package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface VideoPayDao extends JpaRepository<VideoPay, Long>, CrudRepository<VideoPay, Long> {
    VideoPay findAllById(Long id);
    VideoPay findAllByVideoId(Long videoId);
}
