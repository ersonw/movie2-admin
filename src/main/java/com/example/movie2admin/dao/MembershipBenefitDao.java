package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipBenefit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface MembershipBenefitDao extends JpaRepository<MembershipBenefit, Long>, CrudRepository<MembershipBenefit, Long> {
    MembershipBenefit findAllById(Long id);

    Page<MembershipBenefit> findAllByNameLike(String title, Pageable pageable);

    MembershipBenefit findAllByName(String name);
}
