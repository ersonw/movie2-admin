package com.example.movie2admin.dao;

import com.example.movie2admin.entity.CashOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface CashOrderDao extends JpaRepository<CashOrder, Long>, CrudRepository<CashOrder, Long> {
    CashOrder findAllByOrderNo(String orderId);

    @Query(value = "SELECT IFNULL( SUM(cio.total_fee), 0.0 ) FROM cash_order AS mo INNER JOIN cash_in_order as cio ON cio.order_no=mo.order_no AND cio.status = 1 WHERE mo.user_id=:id",nativeQuery = true)
    Double getAllByUserId(long id);
}
