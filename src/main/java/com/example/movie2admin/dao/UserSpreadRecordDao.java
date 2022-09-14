package com.example.movie2admin.dao;

import com.example.movie2admin.entity.UserSpreadRecord;
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
public interface UserSpreadRecordDao extends JpaRepository<UserSpreadRecord, Long>, CrudRepository<UserSpreadRecord, Long> {
    UserSpreadRecord findAllByUserId(long userId);
    UserSpreadRecord findAllByShareUserId(long userId);
//    List<UserSpreadRecord> findAllByShareUserId(long userId);

    Long countAllByShareUserId(long id);

    @Query(value = "SELECT * FROM user_spread_record WHERE share_user_id =:userId AND add_time > :start AND add_time < :end",nativeQuery = true)
    Page<UserSpreadRecord> getUsersSpreadRecordUserList(long userId, long start, long end, Pageable pageable);
    @Query(value = "SELECT * FROM user_spread_record WHERE share_user_id =:userId AND add_time > :start",nativeQuery = true)
    Page<UserSpreadRecord> getUsersSpreadRecordUserList(long userId, long start, Pageable pageable);
    @Query(value = "SELECT * FROM user_spread_record WHERE share_user_id =:userId AND add_time < :end",nativeQuery = true)
    Page<UserSpreadRecord> getUsersSpreadRecordUserListEnd(long userId, long end, Pageable pageable);
    @Query(value = "SELECT * FROM user_spread_record WHERE share_user_id =:userId",nativeQuery = true)
    Page<UserSpreadRecord> getUsersSpreadRecordUserList(long userId, Pageable pageable);
}
