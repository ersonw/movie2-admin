package com.example.movie2admin.dao;

import com.example.movie2admin.entity.DiamondConfig;
import com.example.movie2admin.entity.MembershipConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface MembershipConfigDao extends JpaRepository<MembershipConfig, Long>, CrudRepository<MembershipConfig, Long> {
    MembershipConfig findAllById(long id);
    List<MembershipConfig> findAllByName(String name);

    MembershipConfig findByName(String key);
}
