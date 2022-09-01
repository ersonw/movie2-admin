package com.example.movie2admin.dao;

import com.example.movie2admin.entity.Video;
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
public interface VideoDao extends JpaRepository<Video, Long>, CrudRepository<Video, Long> {
    Video findAllByShareId(String shareId);
    Video findAllById(long id);
    Page<Video> findAllByTitle(String title, Pageable pageable);
    @Query(value = "SELECT * FROM `video` WHERE title LIKE :title AND status > -1", nativeQuery = true)
    Page<Video> getAllByTitle(String title, Pageable pageable);
    @Query(value = "SELECT * FROM `video` WHERE status > -1", nativeQuery = true)
    Page<Video> getAll(Pageable pageable);
    Page<Video> findAllByTitleLikeAndStatus(String title,int status, Pageable pageable);
    @Query(value = "SELECT v.* FROM `video` AS v INNER JOIN video_pay AS vp ON vp.video_id = v.id", nativeQuery = true)
    Page<Video> getAllPrice(Pageable pageable);
    @Query(value = "SELECT v.* FROM `video` AS v INNER JOIN video_pay AS vp ON vp.video_id = v.id WHERE v.title LIKE :title", nativeQuery = true)
    Page<Video> getAllPriceByTitle(String title,Pageable pageable);

    //无跟条件
    Page<Video> findAllByStatus(int status, Pageable pageable);
    //单独vodClass条件
    Page<Video> findAllByVodClassAndStatus(long vodClass,int status, Pageable pageable);
    //单独produced条件
    @Query(value = "SELECT v.* FROM `video_produced_record` AS vpr INNER JOIN `video` AS v ON vpr.video_id=v.id AND v.status=1 WHERE vpr.produced_id=:id", nativeQuery = true)
    Page<Video> getAllByProduced(long id,Pageable pageable);
    //vodClass produced 并发条件
    @Query(value = "SELECT v.* FROM `video_produced_record` AS vpr INNER JOIN `video` AS v ON vpr.video_id=v.id AND v.status=1 AND v.vod_class=:vodClass WHERE vpr.produced_id=:producedId", nativeQuery = true)
    Page<Video> getAllByVodClassAndProduced(long vodClass,long producedId,Pageable pageable);


    //全部最热
    @Query(value = "SELECT v.*,(v.plays+(SELECT COUNT(*) FROM `video_play` WHERE video_id= v.id)) AS c FROM `video` v WHERE  v.status=:status ORDER BY c DESC", nativeQuery = true)
    Page<Video> getVideoByStatus(int status,Pageable pageable);
    //vodClass produced 并发条件
    @Query(value = "SELECT v.*,(v.plays+(SELECT COUNT(*) FROM `video_play` WHERE video_id= v.id)) AS c FROM `video` v WHERE v.vod_class=:vodClass AND v.status=1 ORDER BY c DESC", nativeQuery = true)
    Page<Video> getVideoByVodClass(long vodClass,Pageable pageable);
    //vodClass produced 并发条件
    @Query(value = "SELECT v.*,(v.plays+(SELECT COUNT(*) FROM `video_play` WHERE video_id= v.id)) AS c FROM `video_produced_record` AS vpr INNER JOIN `video` AS v ON vpr.video_id=v.id AND v.status=1 WHERE vpr.produced_id=:producedId ORDER BY c DESC", nativeQuery = true)
    Page<Video> getVideoByProduced(long producedId,Pageable pageable);
    //vodClass produced 并发条件
    @Query(value = "SELECT v.*,(v.plays+(SELECT COUNT(*) FROM `video_play` WHERE video_id= v.id)) AS c  FROM `video_produced_record` AS vpr INNER JOIN `video` AS v ON vpr.video_id=v.id AND v.status=1 AND v.vod_class=:vodClass WHERE vpr.produced_id=:producedId ORDER BY c DESC", nativeQuery = true)
    Page<Video> getVideoByVodClassAndProduced(long vodClass,long producedId,Pageable pageable);

    @Query(value = "SELECT v.* FROM `video_concentration_list` AS vcl INNER JOIN `video` v ON v.id=vcl.id AND v.status=1 WHERE vcl.concentration_id =:concentrationId", nativeQuery = true)
    Page<Video> getVideoByConcentrations(long concentrationId,Pageable pageable);

    @Query(value = "SELECT v.* FROM `video` AS v INNER JOIN `video_pay` vp ON vp.video_id=v.id AND vp.amount > 0 WHERE  v.status=:status", nativeQuery = true)
    Page<Video> getVideoByPay(int status,Pageable pageable);
    @Query(value = "SELECT v1.* FROM (SELECT v.*,(SELECT amount FROM `video_pay` WHERE video_id=v.id) AS amount FROM `video` AS v WHERE  v.status=1) AS v1 WHERE `amount`=0 OR `amount` IS NULL", nativeQuery = true)
    Page<Video> getVideoByPay(Pageable pageable);
    @Query(value = "SELECT v.*,(v.plays+(SELECT COUNT(*) FROM `video_play` WHERE video_id= v.id AND add_time >:time)) AS c  FROM `video` AS v ORDER BY c DESC", nativeQuery = true)
    Page<Video> getVideoByRank(long time,Pageable pageable);
    @Query(value = "SELECT v.*,(v.plays+(SELECT COUNT(*) FROM `video_play` WHERE video_id= v.id AND add_time >:time)) AS c  FROM `video_class` AS vc INNER JOIN `video` v ON v.vod_class=vc.id WHERE vc.id=:vodClass ORDER BY c DESC", nativeQuery = true)
    Page<Video> getVideoByRank(long time,long vodClass,Pageable pageable);
    @Query(value = "SELECT (v.plays+(SELECT COUNT(*) FROM `video_play` WHERE video_id= v.id AND add_time >:time)) FROM `video` AS v WHERE v.id=:videoId", nativeQuery = true)
    long getVideoByRank(long time,long videoId);
//    @Query(value = "SELECT * FROM video WHERE id NOT IN (    SELECT video_id FROM video_concentration_list WHERE 1) AND title LIKE :title", nativeQuery = true)
    @Query(value = "SELECT * FROM video WHERE id NOT IN (    SELECT video_id FROM video_concentration_list as vcl INNER JOIN video AS v on v.id=vcl.video_id AND v.status =1 WHERE vcl.concentration_id = :id) AND title LIKE :title", nativeQuery = true)
    Page<Video> getActiveList(long id, String title, Pageable pageable);
//    @Query(value = "SELECT * FROM video WHERE id NOT IN (    SELECT video_id FROM video_concentration_list WHERE 1)", nativeQuery = true)
    @Query(value = "SELECT * FROM video WHERE id NOT IN (    SELECT video_id FROM video_concentration_list as vcl INNER JOIN video AS v on v.id=vcl.video_id AND v.status =1 WHERE vcl.concentration_id = :id)", nativeQuery = true)
    Page<Video> getActiveList(long id, Pageable pageable);
    @Query(value = "SELECT * FROM video WHERE id NOT IN (    SELECT video_id FROM video_produced_record as vcl INNER JOIN video AS v on v.id=vcl.video_id AND v.status =1 WHERE 1) AND title LIKE :title", nativeQuery = true)
    Page<Video> getVideoSourceActiveList(String title, Pageable pageable);
    @Query(value = "SELECT * FROM video WHERE id NOT IN (    SELECT video_id FROM video_produced_record as vcl INNER JOIN video AS v on v.id=vcl.video_id AND v.status =1 WHERE 1)", nativeQuery = true)
    Page<Video> getVideoSourceActiveList( Pageable pageable);
    @Modifying
    @Query(value = "DELETE vc.*,svct.*,svc.*,svcr.*,svd.*,svf.*,svl.*,svp.*,svs.*,svcl.*\n" +
            "FROM video as vc \n" +
            "LEFT JOIN video_comment as svct ON svct.video_id=vc.id \n" +
            "LEFT JOIN video_comment_like as svc ON svc.comment_id=svct.id \n" +
            "LEFT JOIN video_concentration_list as svcl ON svcl.video_id=vc.id \n" +
            "LEFT JOIN video_like as svcr ON svcl.video_id=vc.id \n" +
            "LEFT JOIN video_pay as svd ON svd.video_id=vc.id \n" +
            "LEFT JOIN video_pay_record as svf ON svf.pay_id=svd.id\n" +
            "LEFT JOIN video_play as svl ON svl.video_id=vc.id \n" +
            "LEFT JOIN video_produced_record as svp ON svp.video_id=vc.id \n" +
            "LEFT JOIN video_scale as svs ON svs.video_id=vc.id \n" +
            "WHERE vc.id=:id", nativeQuery = true)
    void removeAllById(long id);
}
