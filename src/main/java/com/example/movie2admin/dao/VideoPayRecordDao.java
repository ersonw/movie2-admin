package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoPayRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface VideoPayRecordDao extends JpaRepository<VideoPayRecord, Long>, CrudRepository<VideoPayRecord, Long> {
    VideoPayRecord findAllByUserId(long id);
    VideoPayRecord findAllByUserIdAndPayId(long userId, long videoId);
    @Query(value = "SELECT vpr.* FROM video_pay_record as vpr INNER JOIN video_pay AS vp ON vp.id = vpr.pay_id INNER JOIN video as v ON v.id = vp.video_id AND v.title LIKE :title", nativeQuery = true)
    Page<VideoPayRecord> getAllByTitle(String title, Pageable pageable);
    @Query(value = "DELETE FROM `video_pay_record` WHERE pay_id = :id",nativeQuery = true)
    @Modifying
    void deleteAllByPay(long id);

    Long countAllByUserId(long id);
}
