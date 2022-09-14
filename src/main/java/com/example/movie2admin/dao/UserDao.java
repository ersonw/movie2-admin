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
    User findAllById(long id);
    Page<User> findAllById(long id, Pageable pageable);

    User findByUsername(String username);

    User findByPhone(String username);

    @Query(value = "SELECT u.* FROM `membership_expired` AS me INNER JOIN `user` u ON u.id=me.user_id WHERE me.user_id=:userId AND me.expired >:time ", nativeQuery = true)
    User getAllByMembership(long userId, long time);

    Page<User> findAllByNickname(String s, Pageable pageable);

    Page<User> findAllByUsername(String s, Pageable pageable);

    Page<User> findAllByPhone(String s, Pageable pageable);
    Page<User> findAllByUsernameOrNicknameOrPhone(String username, String nickname,String phone,Pageable pageable);
    @Query(value = "SELECT usr.*,(SELECT COUNT(*) FROM user_spread_record WHERE share_user_id=u.id) AS c FROM `user` as u INNER JOIN user_spread_record AS usr ON usr.share_user_id=u.id WHERE u.id=:title GROUP BY usr.share_user_id ORDER BY c DESC",nativeQuery = true)
    Page<UserSpreadRecord> getUsersSpreadRecordList(long title, Pageable pageable);
    @Query(value = "SELECT usr.*,(SELECT COUNT(*) FROM user_spread_record WHERE share_user_id=u.id) AS c FROM `user` as u INNER JOIN user_spread_record AS usr ON usr.share_user_id=u.id WHERE u.nickname LIKE :title OR u.username LIKE :title OR u.phone LIKE :title GROUP BY usr.share_user_id ORDER BY c DESC",nativeQuery = true)
    Page<UserSpreadRecord> getUsersSpreadRecordList(String title, Pageable pageable);
    @Query(value = "SELECT usr.*,(SELECT COUNT(*) FROM user_spread_record WHERE share_user_id=u.id) AS c FROM `user` as u INNER JOIN user_spread_record AS usr ON usr.share_user_id=u.id GROUP BY usr.share_user_id ORDER BY c DESC",nativeQuery = true)
    Page<UserSpreadRecord> getUsersSpreadRecordList(Pageable pageable);
}
