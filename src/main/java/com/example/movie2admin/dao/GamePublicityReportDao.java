package com.example.movie2admin.dao;

import com.example.movie2admin.entity.GamePublicityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface GamePublicityReportDao extends JpaRepository<GamePublicityReport, Long>, CrudRepository<GamePublicityReport, Long> {
    Long countAllByPublicityId(long id);
    @Query(value = "DELETE gpr.*,gp.* FROM game_publicity AS gp LEFT JOIN game_publicity_report AS gpr ON gpr.publicity_id=gp.id WHERE gp.id=:id",nativeQuery = true)
    @Modifying
    void removeAllById(long id);
}
