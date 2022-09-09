package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GameWater;
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
public interface GameWaterDao extends JpaRepository<GameWater, Long>, CrudRepository<GameWater, Long> {
    GameWater findAllByRecordId(String s);
    @Query(value = "SELECT t1.*\n" +
            "FROM game_water t1\n" +
            "INNER JOIN (\n" +
            "\tSELECT MAX(id) AS id,game_id\n" +
            "\tFROM game_water t1\n" +
            "    WHERE user_id=:userId\n" +
            "\tGROUP BY game_id\n" +
            "\t) t2\n" +
            "ON t2.game_id = t1.game_id\n" +
            "AND t2.id = t1.id order by t1.id desc\n",nativeQuery = true)
    List<GameWater> getAllByUser(long userId);


    @Query(value = "SELECT * FROM game_water WHERE id IN (SELECT gw.id FROM user as u INNER JOIN game_water AS gw ON gw.user_id=u.id  WHERE u.id = :id)",nativeQuery = true)
    Page<GameWater> getGameWaterListById(long id,Pageable pageable);
    @Query(value = "SELECT * FROM game_water WHERE id IN (SELECT gw.id FROM user as u INNER JOIN game_water AS gw ON gw.user_id=u.id AND gw.record_time < :end AND gw.record_time > :start WHERE u.id = :id)",nativeQuery = true)
    Page<GameWater> getGameWaterListById(long id,long start,long end,Pageable pageable);
    @Query(value = "SELECT * FROM game_water WHERE id IN (SELECT gw.id FROM user as u INNER JOIN game_water AS gw ON gw.user_id=u.id AND gw.record_time < :end WHERE u.id = :id)",nativeQuery = true)
    Page<GameWater> getGameWaterListByIdEnd(long id,long end,Pageable pageable);
    @Query(value = "SELECT * FROM game_water WHERE id IN (SELECT gw.id FROM user as u INNER JOIN game_water AS gw ON gw.user_id=u.id AND gw.record_time > :start WHERE u.id = :id)",nativeQuery = true)
    Page<GameWater> getGameWaterListById(long id,long start,Pageable pageable);



    @Query(value = "SELECT IFNULL( SUM(valid_bet), 0 ) FROM game_water WHERE user_id=:id",nativeQuery = true)
    Long getValidBet(long id);
    @Query(value = "SELECT IFNULL( SUM(profit), 0 ) FROM game_water WHERE user_id=:id",nativeQuery = true)
    Long getProfit(long id);

    @Query(value = "SELECT IFNULL( SUM(tax), 0 ) FROM game_water WHERE user_id=:id",nativeQuery = true)
    Long getTax(long id);
    @Query(value = "SELECT IFNULL( SUM(valid_bet), 0 ) FROM game_water WHERE user_id=:id AND record_time > :start AND record_time < :end",nativeQuery = true)
    Long getValidBet(long id,long start, long end);

    @Query(value = "SELECT IFNULL( SUM(profit), 0 ) FROM game_water WHERE user_id=:id AND record_time > :start AND record_time < :end",nativeQuery = true)
    Long getProfit(long id,long start, long end);

    @Query(value = "SELECT IFNULL( SUM(tax), 0 ) FROM game_water WHERE user_id=:id AND record_time > :start AND record_time < :end",nativeQuery = true)
    Long getTax(long id,long start, long end);
    @Query(value = "SELECT IFNULL( SUM(valid_bet), 0 ) FROM game_water WHERE user_id=:id AND record_time < :end",nativeQuery = true)
    Long getValidBetByEnd(long id,long end);

    @Query(value = "SELECT IFNULL( SUM(profit), 0 ) FROM game_water WHERE user_id=:id AND record_time < :end",nativeQuery = true)
    Long getProfitByEnd(long id, long end);

    @Query(value = "SELECT IFNULL( SUM(tax), 0 ) FROM game_water WHERE user_id=:id AND record_time < :end",nativeQuery = true)
    Long getTaxByEnd(long id,long end);
    @Query(value = "SELECT IFNULL( SUM(valid_bet), 0 ) FROM game_water WHERE user_id=:id AND record_time > :start",nativeQuery = true)
    Long getValidBet(long id,long start);

    @Query(value = "SELECT IFNULL( SUM(profit), 0 ) FROM game_water WHERE user_id=:id AND record_time > :start",nativeQuery = true)
    Long getProfit(long id,long start);

    @Query(value = "SELECT IFNULL( SUM(tax), 0 ) FROM game_water WHERE user_id=:id AND record_time > :start",nativeQuery = true)
    Long getTax(long id,long start);

    @Query(value = "SELECT IFNULL( MIN(record_time), 0 ) FROM game_water WHERE user_id = :id LIMIT 1",nativeQuery = true)
    Long getFirstTime(long id);
    @Query(value = "SELECT IFNULL( MAX(record_time), 0 ) FROM game_water WHERE user_id = :id LIMIT 1",nativeQuery = true)
    Long getLastTime(long id);
}
