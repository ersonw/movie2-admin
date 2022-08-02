package com.example.movie2admin.dao;

import com.example.movie2admin.entity.VideoPayRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface VideoPayRecordDao extends JpaRepository<VideoPayRecord, Long>, CrudRepository<VideoPayRecord, Long> {
    VideoPayRecord findAllByUserId(long id);
    VideoPayRecord findAllByUserIdAndPayId(long userId, long videoId);
}
