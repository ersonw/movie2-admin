package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MembershipExperienceDao extends JpaRepository<MembershipExperience, Long>, CrudRepository<MembershipExperience, Long> {
    long countByUserId(long userId);
    @Query(value = "SELECT IFNULL( SUM(experience), 0 )  FROM `membership_experience` WHERE user_id=:userId",nativeQuery = true)
    Long getAllByUserId(long userId);
}
