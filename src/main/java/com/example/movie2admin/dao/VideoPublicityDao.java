package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoPublicity;
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
public interface VideoPublicityDao extends JpaRepository<VideoPublicity, Long>, CrudRepository<VideoPublicity, Long> {
    List<VideoPublicity> findAllByStatus(int status);
    VideoPublicity findAllById(long id);
    @Modifying
    @Query(value = "DELETE vp.*,vpr.* FROM video_publicity as vp LEFT JOIN video_publicity_report as vpr ON vpr.publicity_id=vp.id WHERE vp.id=:id", nativeQuery = true)
    void removeAllById(long id);
    Page<VideoPublicity> findAllByNameLike(String s, Pageable pageable);

    List<VideoPublicity> findAllByName(String name);
}
