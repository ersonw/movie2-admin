package com.example.movie2admin.dao;

import com.example.movie2admin.entity.DiamondOrder;
import com.example.movie2admin.entity.GameOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DiamondOrderDao extends JpaRepository<DiamondOrder, Long>, CrudRepository<DiamondOrder, Long> {
    DiamondOrder findAllByOrderNo(String orderId);

    Page<DiamondOrder> findAllByOrderNoLike(String s, Pageable pageable);

    @Query(value = "SELECT IFNULL( SUM(cio.total_fee), 0.0 ) FROM diamond_order AS mo INNER JOIN cash_in_order as cio ON cio.order_no=mo.order_no AND cio.status = 1 WHERE mo.user_id=:id",nativeQuery = true)
    Double getAllByUserId(long id);

    @Query(value = "SELECT IFNULL( SUM(cio.total_fee), 0.0 ) FROM diamond_order AS go1 INNER JOIN cash_in_order as cio ON cio.order_no=go1.order_no AND cio.status = 1 ",nativeQuery = true)
    Double getAll();
    @Query(value = "SELECT IFNULL( SUM(cio.total_fee), 0.0 ) FROM diamond_order AS go1 INNER JOIN cash_in_order as cio ON cio.order_no=go1.order_no AND cio.status = 1 WHERE go1.add_time>:t1",nativeQuery = true)
    Double getAll(long t1);
    @Query(value = "SELECT IFNULL( SUM(cio.total_fee), 0.0 ) FROM diamond_order AS go1 INNER JOIN cash_in_order as cio ON cio.order_no=go1.order_no AND cio.status = 1 WHERE go1.add_time>:t1 AND go1.add_time<:t2",nativeQuery = true)
    Double getAll(long t1, long t2);
}
