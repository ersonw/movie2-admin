package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipExpired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MembershipExpiredDao extends JpaRepository<MembershipExpired, Long>, CrudRepository<MembershipExpired, Long> {
    MembershipExpired findAllById(Long id);
    MembershipExpired findAllByUserId(Long userId);
}
