package com.example.movie2admin.dao;

import com.example.movie2admin.entity.User;
import com.example.movie2admin.entity.UserSpreadRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserDao extends JpaRepository<User, Long>, CrudRepository<User, Long> {
    Long countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(long start, long end);
    Long countAllByAddTimeGreaterThanEqual(long start);
    User findAllById(long id);
    Page<User> findAllById(long id, Pageable pageable);

    User findByUsername(String username);

    User findByPhone(String username);

    @Query(value = "SELECT u.* FROM `membership_expired` AS me INNER JOIN `user` u ON u.id=me.user_id WHERE me.user_id=:userId AND me.expired >:time ", nativeQuery = true)
    User getAllByMembership(long userId, long time);
    @Query(value = "SELECT u.* FROM `user` AS u INNER JOIN user_robot ur ON u.id = ur.user_id", nativeQuery = true)
    Page<User> getUserRobotList(Pageable pageable);
    @Query(value = "SELECT * FROM `user` AS u WHERE NOT EXISTS (SELECT * FROM user_robot WHERE user_id = u.id)", nativeQuery = true)
    Page<User> getUserList(Pageable pageable);
    @Query(value = "SELECT * FROM `user` AS u WHERE NOT EXISTS (SELECT * FROM user_robot WHERE user_id = u.id) AND (u.username like :title OR u.nickname like :title OR u.phone like :title)", nativeQuery = true)
    Page<User> getUserList(String title, Pageable pageable);

    Page<User> findAllByNickname(String s, Pageable pageable);

    Page<User> findAllByUsername(String s, Pageable pageable);

    Page<User> findAllByPhone(String s, Pageable pageable);
    Page<User> findAllByUsernameOrNicknameOrPhone(String username, String nickname,String phone,Pageable pageable);


}
