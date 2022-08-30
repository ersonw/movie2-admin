package com.example.movie2admin.dao;

import com.example.movie2admin.entity.PublicizeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface PublicizeReportDao extends JpaRepository<PublicizeReport, Long>, CrudRepository<PublicizeReport, Long> {
    Long countAllByPublicityId(long id);
    PublicizeReport findAllById(long id);
}
