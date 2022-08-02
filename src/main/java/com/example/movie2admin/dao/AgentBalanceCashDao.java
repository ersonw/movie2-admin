package com.example.movie2admin.dao;

import com.example.movie2admin.entity.AgentBalanceCash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AgentBalanceCashDao extends JpaRepository<AgentBalanceCash, Long>, CrudRepository<AgentBalanceCash, Long> {
}
