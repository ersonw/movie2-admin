package com.example.movie2admin.dao;

import com.example.movie2admin.entity.AgentRebate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface AgentRebateDao extends JpaRepository<AgentRebate, Long>, CrudRepository<AgentRebate, Long> {
    @Query(value = "SELECT IFNULL( SUM(amount), 0 ) FROM agent_rebate WHERE order_id=:id AND status > 0",nativeQuery = true)
    Double getAllByOrderId(long id);
    @Query(value = "UPDATE agent_rebate SET `status`=:status,update_time=:time WHERE order_id = :id",nativeQuery = true)
    @Modifying
    void setOrderByUsers(int status, long id, long time);
}
