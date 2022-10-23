package com.example.movie2admin.dao;

import com.example.movie2admin.entity.UserShareRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserShareRecordDao extends JpaRepository<UserShareRecord, Long>, CrudRepository<UserShareRecord, Long> {
    List<UserShareRecord> findAllByTypeAndUserId(String type, long id);
}
