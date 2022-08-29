package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoPlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface VideoPlayDao extends JpaRepository<VideoPlay, Long>, CrudRepository<VideoPlay, Long> {
    long countAllByVideoId(long videoId);
    @Modifying
    @Query(value = "delete from `video_play` WHERE user_id=:id", nativeQuery = true)
    void removeAllByUserId(long id);
    @Modifying
    @Query(value = "delete from `video_play` WHERE video_id=:id", nativeQuery = true)
    void removeAllByVideoId(long id);
    @Query(value = "SELECT * FROM (SELECT (vs.video_time / v.vod_duration * 100) as c FROM `video_scale` AS vs INNER JOIN video AS v ON v.id=vs.video_id WHERE vs.video_id=:id AND vs.video_time > 30) AS s WHERE s.c > 0", nativeQuery = true)
    Double getScale(long id);
}
