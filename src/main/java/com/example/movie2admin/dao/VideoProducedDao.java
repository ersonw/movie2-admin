package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoProduced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface VideoProducedDao extends JpaRepository<VideoProduced, Long>, CrudRepository<VideoProduced, Long> {
    List<VideoProduced> findAllByStatus(int status);

    Page<VideoProduced> findAllByNameLike(String s, Pageable pageable);
    @Modifying
    @Query(value = "DELETE vp.*,vpr.* FROM video_produced as vp LEFT JOIN video_produced_record as vpr ON vpr.produced_id=vp.id WHERE vp.id=:id", nativeQuery = true)
    void removeAllById(long id);

    List<VideoProduced> findAllByName(String name);
    VideoProduced findAllById(long id);
}
