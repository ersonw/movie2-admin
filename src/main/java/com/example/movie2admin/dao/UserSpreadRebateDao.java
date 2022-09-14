package com.example.movie2admin.dao;

import com.example.movie2admin.entity.UserSpreadRebate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserSpreadRebateDao extends JpaRepository<UserSpreadRebate, Long>, CrudRepository<UserSpreadRebate, Long> {
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_spread_rebate WHERE order_id=:id AND status > 0",nativeQuery = true)
    Double getAllByOrderId(long id);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_spread_rebate WHERE user_id=:id AND status > 0",nativeQuery = true)
    Object countAllByShareUserId(long id);
    @Query(value = "SELECT IFNULL( SUM(usr.amount), 0 ) FROM user_consume AS uc INNER JOIN user_spread_rebate AS usr ON uc.id = usr.order_id AND usr.user_id=:shareUserId AND usr.status > 0 WHERE uc.user_id=:userId",nativeQuery = true)
    Double getUserSpreadRecord(long shareUserId, long userId);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM user_spread_rebate WHERE status >0 AND user_id=:id",nativeQuery = true)
    Double getUsersSpreadRecordList(long id);
}
