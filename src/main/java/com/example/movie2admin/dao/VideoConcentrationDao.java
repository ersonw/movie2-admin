package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoConcentration;
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
public interface VideoConcentrationDao extends JpaRepository<VideoConcentration, Long>, CrudRepository<VideoConcentration, Long> {
    VideoConcentration findAllById(long id);
    @Query(value = "select vc.* FROM video_concentration as vc  WHERE vc.name LIKE :title", nativeQuery = true)
    Page<VideoConcentration> getAllByName(String title, Pageable pageable);
    @Query(value = "select vc.* FROM video_concentration as vc", nativeQuery = true)
    Page<VideoConcentration> getAll(Pageable pageable);
    @Modifying
    @Query(value = "DELETE vc.*,vcl.* FROM video_concentration as vc LEFT JOIN video_concentration_list as vcl ON vcl.concentration_id=vc.id WHERE vc.id=:id", nativeQuery = true)
    void removeAllById(long id);

    VideoConcentration findByName(String name);
    List<VideoConcentration> findAllByName(String name);
}
