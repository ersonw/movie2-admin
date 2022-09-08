package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GameOutOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GameOutOrderDao extends JpaRepository<GameOutOrder, Long>, CrudRepository<GameOutOrder, Long> {
    GameOutOrder findAllById(long id);
    Page<GameOutOrder> findAllByUserId(long userId, Pageable pageable);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 )  FROM `game_out_order` WHERE user_id=:userId",nativeQuery = true)
    Long getAllBywBalance(long userId);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 )  FROM `game_out_order` WHERE user_id=:userId AND status=:status",nativeQuery = true)
    Long getAllBywBalance(long userId, int status);
    @Query(value = "SELECT IFNULL( SUM(amount), 0 )  FROM `game_out_order` WHERE user_id=:userId AND status <> 1",nativeQuery = true)
    Long getAllByFreezeBalance(long userId);

    Page<GameOutOrder> findAllByOrderNoLike(String s, Pageable pageable);

    Page<GameOutOrder> findAllByOrderNoLikeAndStatus(String s, int status, Pageable pageable);

    Page<GameOutOrder> findAllByStatus(int status, Pageable pageable);
}
