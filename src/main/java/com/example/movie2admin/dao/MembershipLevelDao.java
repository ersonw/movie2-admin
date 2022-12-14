package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MembershipLevelDao extends JpaRepository<MembershipLevel, Long>, CrudRepository<MembershipLevel, Long> {
    MembershipLevel findAllById(Long id);
    MembershipLevel findAllByLevel(int level);

    Page<MembershipLevel> findAllByLevel(int level, Pageable pageable);

    MembershipLevel findByLevel(int i);
}
