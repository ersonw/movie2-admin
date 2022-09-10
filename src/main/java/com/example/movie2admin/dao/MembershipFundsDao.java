package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipFunds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MembershipFundsDao  extends JpaRepository<MembershipFunds, Long>, CrudRepository<MembershipFunds, Long> {
    Page<MembershipFunds> findAllByUserId(long id, Pageable pageable);
    Page<MembershipFunds> findAllByUserIdAndAddTimeGreaterThanEqualAndAddTimeLessThanEqual(long userId, long start, long end, Pageable pageable);
    Page<MembershipFunds> findAllByUserIdAndAddTimeLessThanEqual(long userId,  long end, Pageable pageable);
    Page<MembershipFunds> findAllByUserIdAndAddTimeGreaterThanEqual(long userId, long start, Pageable pageable);
}
