package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipExpired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MembershipExpiredDao extends JpaRepository<MembershipExpired, Long>, CrudRepository<MembershipExpired, Long> {
    Long countAllByAddTimeGreaterThanEqualAndAddTimeLessThanEqual(long start, long end);
    Long countAllByAddTimeGreaterThanEqual(long start);
    MembershipExpired findAllById(Long id);
    MembershipExpired findAllByUserId(Long userId);
    @Query(value = "SELECT me.* FROM `user` AS u INNER JOIN membership_expired AS me ON u.id=me.user_id  WHERE u.nickname LIKE :title",nativeQuery = true)
    Page<MembershipExpired> getMembershipList(String title, Pageable pageable);
    @Query(value = "SELECT me.* FROM membership_expired AS me INNER JOIN `user` AS u ON u.id=me.user_id ",nativeQuery = true)
    Page<MembershipExpired> getMembershipList(Pageable pageable);
}
