package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoProducedRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface VideoProducedRecordDao extends JpaRepository<VideoProducedRecord, Long>, CrudRepository<VideoProducedRecord, Long> {
    VideoProducedRecordDao findAllById(long id);
    Long countAllByProducedId(long id);

    Page<VideoProducedRecord> findAllByProducedId(long id, Pageable pageable);
    @Query(value = "SELECT vpr.* FROM `video_produced_record` vpr INNER JOIN  video AS v ON vpr.video_id=v.id AND v.title LIKE :title WHERE vpr.produced_id =:id", nativeQuery = true)
    Page<VideoProducedRecord> getAllByTitle(String title, long id, Pageable pageable);
//    @Modifying
//    @Query(value = "SELECT vc.* FROM (SELECT * FROM `video` WHERE status=:status AND vod_class > 0 GROUP BY `vod_class`) v LEFT JOIN `video_class` vc ON v.vod_class=vc.id ", nativeQuery = true)
//    List<VideoClass> findAllByStatus(int status);
}
