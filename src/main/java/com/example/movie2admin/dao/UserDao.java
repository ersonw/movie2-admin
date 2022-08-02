package com.example.movie2admin.dao;

import com.example.movie2admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserDao extends JpaRepository<User, Long>, CrudRepository<User, Long> {
    User findAllById(long id);

    User findByUsername(String username);

    User findByPhone(String username);

    @Query(value = "SELECT u.* FROM `membership_expired` AS me INNER JOIN `user` u ON u.id=me.user_id WHERE me.user_id=:userId AND me.expired >:time ", nativeQuery = true)
    User getAllByMembership(long userId, long time);
}
