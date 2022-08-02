package com.example.movie2admin.dao;

import com.example.movie2admin.entity.ShortVideoScale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface ShortVideoScaleDao extends JpaRepository<ShortVideoScale, Long>, CrudRepository<ShortVideoScale, Long> {
    List<ShortVideoScale> findAllByVideoIdAndUserIdAndAddTimeGreaterThanEqual(Long videoId, Long userId, long time);
}
