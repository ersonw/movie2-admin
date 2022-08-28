package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoConcentrationList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface VideoConcentrationListDao extends JpaRepository<VideoConcentrationList, Long>, CrudRepository<VideoConcentrationList, Long> {
    @Query(value = "SELECT vcl.* FROM `video_concentration_list` AS vcl INNER JOIN `video` v ON v.id=vcl.video_id INNER JOIN `video_concentration` vc ON vc.id=vcl.concentration_id GROUP BY concentration_id", nativeQuery = true)
    List<VideoConcentrationList> getAllByGroup();
    Long countAllByConcentrationId(long id);
    Page<VideoConcentrationList> findAllByConcentrationId(long id, Pageable pageable);
    List<VideoConcentrationList> findAllByConcentrationIdAndVideoId(long concentration_id, long videoId);
    @Query(value = "SELECT vcl.* FROM `video_concentration_list` vcl INNER JOIN  video AS v ON vcl.video_id=v.id AND v.title LIKE :title WHERE vcl.concentration_id =:id", nativeQuery = true)
    Page<VideoConcentrationList> getAllByTitle(String title,long id, Pageable pageable);
}
