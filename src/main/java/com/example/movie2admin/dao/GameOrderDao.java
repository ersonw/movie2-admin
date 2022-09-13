package com.example.movie2admin.dao;

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
public interface GameOrderDao extends JpaRepository<GameOrder, Long>, CrudRepository<GameOrder, Long> {
    GameOrder findAllByOrderNo(String orderNo);
    Page<GameOrder> findAllByOrderNoLike(String orderNo, Pageable pageable);
    GameOrder findAllById(long id);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM game_order WHERE user_id=:id",nativeQuery = true)
    Long getCashIn(long id);
    @Query(value = "SELECT IFNULL( SUM(cio.total_fee), 0.0 ) FROM game_order AS go1 INNER JOIN cash_in_order as cio ON cio.order_no=go1.order_no AND cio.status = 1 WHERE go1.user_id=:id",nativeQuery = true)
    Double getAllByUserId(long id);
}
