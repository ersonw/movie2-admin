package com.example.movie2admin.dao;

import com.example.movie2admin.entity.MembershipGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MembershipGradeDao extends JpaRepository<MembershipGrade, Long>, CrudRepository<MembershipGrade, Long> {
    @Query(value = "SELECT * FROM `membership_grade` WHERE status=1 ORDER BY `mini` ASC ",nativeQuery = true)
    List<MembershipGrade> getAllGrades();
    MembershipGrade findAllById(long id);
    Page<MembershipGrade> findAllByNameLike(String s, Pageable pageable);

    MembershipGrade findAllByName(String name);
}
