package com.example.movie2admin.dao;

import com.example.movie2admin.entity.ShortVideoCommentReport;
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
public interface ShortVideoCommentReportDao extends JpaRepository<ShortVideoCommentReport, Long>, CrudRepository<ShortVideoCommentReport, Long> {
    Long countAllByCommentIdAndState(long commentId, int state);
    @Query(value = "SELECT svcr.* FROM short_video_comment AS svc INNER JOIN `short_video_comment_report` AS svcr ON svcr.comment_id=svc.id AND svcr.state = 0 WHERE svc.text LIKE :title group by svcr.comment_id order by svcr.add_time desc", nativeQuery = true)
    Page<ShortVideoCommentReport> getAllByAudit(String title, Pageable pageable);
    @Query(value = "SELECT svcr.* FROM short_video_comment AS svc INNER JOIN `short_video_comment_report` AS svcr ON svcr.comment_id=svc.id AND svcr.state = 0 group by svcr.comment_id order by svcr.add_time desc", nativeQuery = true)
    Page<ShortVideoCommentReport> getAllByAudit(Pageable pageable);

    List<ShortVideoCommentReport> findAllByCommentId(long commentId);
}
