package com.example.movie2admin.dao;

import com.example.movie2admin.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserFollowDao extends JpaRepository<UserFollow, Long>, CrudRepository<UserFollow, Long> {
    UserFollow findAllByUserIdAndToUserId(Long userId, Long toUserId);
    Long countAllByUserId(Long userId);
    Long countAllByToUserId(Long userId);
    Long countAllByToUserIdAndState(Long userId, int state);
}
