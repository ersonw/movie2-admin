package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MembershipOrderDao extends JpaRepository<MembershipOrder, Long>, CrudRepository<MembershipOrder, Long> {
    MembershipOrder findAllByOrderNo(String orderId);
    @Query(value = "SELECT IFNULL( COUNT(*), 0 ) FROM membership_order AS mo INNER JOIN cash_in_order AS cio ON cio.order_no=mo.order_no AND cio.status=1 WHERE mo.user_id=:id",nativeQuery = true)
    Long getOrders(long id);
}
