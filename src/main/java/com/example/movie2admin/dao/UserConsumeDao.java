package com.example.movie2admin.dao;

import com.example.movie2admin.entity.UserConsume;
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
public interface UserConsumeDao extends JpaRepository<UserConsume, Long>, CrudRepository<UserConsume, Long> {
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_consume WHERE user_id=:id",nativeQuery = true)
    Double getAllByUserId(long id);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_consume WHERE user_id=:id AND add_time > :t1 AND add_time < :t2",nativeQuery = true)
    Double getAllByUserId(long id,long t1,long t2);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_consume WHERE user_id=:id AND add_time > :t1",nativeQuery = true)
    Double getAllByUserId(long id,long t1);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_consume WHERE user_id=:id AND add_time < :t2",nativeQuery = true)
    Double getAllByUserIdEnd(long id,long t2);
    Long countAllByUserId(long id);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN agent_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 AND usr.add_time > :t1 AND usr.add_time < :t2 WHERE uc.user_id=:id",nativeQuery = true)
    Double getAllByAgent(long id, long t1, long t2);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN agent_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 AND usr.add_time > :t1 WHERE uc.user_id=:id ",nativeQuery = true)
    Double getAllByAgent(long id, long t1);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN agent_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 AND usr.add_time < :t2 WHERE uc.user_id=:id",nativeQuery = true)
    Double getAllByAgentEnd(long id, long t2);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN agent_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 WHERE uc.user_id=:id",nativeQuery = true)
    Double getAllByAgent(long id);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN user_spread_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 AND usr.add_time > :t1 AND usr.add_time < :t2 WHERE uc.user_id=:id",nativeQuery = true)
    Double getAllByUsers(long id, long t1, long t2);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN user_spread_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 AND usr.add_time > :t1 WHERE uc.user_id=:id ",nativeQuery = true)
    Double getAllByUsers(long id, long t1);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN user_spread_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 AND usr.add_time < :t2 WHERE uc.user_id=:id",nativeQuery = true)
    Double getAllByUsersEnd(long id, long t2);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume as uc INNER JOIN user_spread_rebate as usr ON uc.id=usr.order_id AND usr.status = 1 WHERE uc.user_id=:id",nativeQuery = true)
    Double getAllByUsers(long id);

    @Query(value = "SELECT * FROM `user_consume` WHERE user_id =:userId AND add_time > :start AND add_time < :end",nativeQuery = true)
    Page<UserConsume> getUsersConsumeListUser(long userId, long start, long end, Pageable pageable);
    @Query(value = "SELECT * FROM `user_consume` WHERE user_id =:userId AND add_time > :start ",nativeQuery = true)
    Page<UserConsume> getUsersConsumeListUser(long userId, long start, Pageable pageable);
    @Query(value = "SELECT * FROM `user_consume` WHERE user_id =:userId AND add_time < :end",nativeQuery = true)
    Page<UserConsume> getUsersConsumeListUserEnd(long userId, long end, Pageable pageable);
    @Query(value = "SELECT * FROM `user_consume` WHERE user_id =:userId ",nativeQuery = true)
    Page<UserConsume> getUsersConsumeListUser(long userId, Pageable pageable);

    @Query(value = "UPDATE user_spread_rebate SET `status`=1,update_time=:time WHERE order_id = :id ",nativeQuery = true)
    @Modifying
    void makeupOrderByUsers(long id, long time);
    @Query(value = "UPDATE user_spread_rebate SET `status`=-1,update_time=:time WHERE order_id = :id AND user_id <> :userId ",nativeQuery = true)
    @Modifying
    void makeDownOrderByUsers(long id, long userId,long time);
    @Query(value = "UPDATE user_spread_rebate SET `status`=-1,update_time=:time WHERE order_id = :id",nativeQuery = true)
    @Modifying
    void makeDownOrderByUsers(long id,long time);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_consume WHERE user_id=:userId AND status > 0",nativeQuery = true)
    Double getUserSpreadRecord(long userId);
}
