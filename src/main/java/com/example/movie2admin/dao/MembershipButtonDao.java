package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipButton;
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
public interface MembershipButtonDao extends JpaRepository<MembershipButton, Long>, CrudRepository<MembershipButton, Long> {
    MembershipButton findAllById(Long id);
    @Query(value = "SELECT * FROM `membership_button` WHERE status=1 ORDER BY price ASC ",nativeQuery = true)
    List<MembershipButton> getAllButtons();

    Page<MembershipButton> findAllByAmountGreaterThanEqual(long title, Pageable pageable);
}
