package com.example.movie2admin.dao;

import com.example.movie2admin.entity.ShortVideoShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ShortVideoShareDao extends JpaRepository<ShortVideoShare, Long>, CrudRepository<ShortVideoShare, Long> {
    long countAllByVideoId(long videoId);
    @Query(value = "SELECT svs.* FROM user AS u INNER JOIN `short_video_share` as svs ON svs.user_id=u.id OR svs.to_user_id=u.id WHERE u.nickname LIKE :title", nativeQuery = true)
    Page<ShortVideoShare> getShareVideo(String title, Pageable pageable);
    @Query(value = "SELECT * FROM `short_video_share`", nativeQuery = true)
    Page<ShortVideoShare> getShareVideo(Pageable pageable);
}
