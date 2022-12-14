package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GameOutCard;
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
public interface GameOutCardDao extends JpaRepository<GameOutCard, Long>, CrudRepository<GameOutCard, Long> {
    GameOutCard findAllById(long id);
    GameOutCard findAllByIdAndUserId(long id,long userId);
    GameOutCard findAllByUserIdAndCard(long userId, String card);
    Page<GameOutCard> findAllByUserId(long userId, Pageable pageable);
    List<GameOutCard> findAllByUserId(long userId);

    @Query(value = "SELECT goc.* FROM user AS u INNER JOIN game_out_card AS goc ON goc.user_id = u.id WHERE u.id=:title",nativeQuery = true)
    Page<GameOutCard> getGameWithdrawCard(long title, Pageable pageable);
    @Query(value = "SELECT goc.* FROM user AS u INNER JOIN game_out_card AS goc ON goc.user_id = u.id WHERE u.phone LIKE :title",nativeQuery = true)
    Page<GameOutCard> getGameWithdrawCard(String title, Pageable pageable);
}
