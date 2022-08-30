package com.example.movie2admin.dao;

import com.example.movie2admin.entity.Publicize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface PublicizeDao extends JpaRepository<Publicize, Long>, CrudRepository<Publicize, Long> {
    List<Publicize> findAllByStatus(int status);
    Page<Publicize> findAllByStatus(int status, Pageable pageable);
    List<Publicize> findAllByPage(int page);
    List<Publicize> findAllByPageAndStatus(int page, int status);

    Page<Publicize> findAllByNameLike(String s, Pageable pageable);
    Publicize findAllById(long id);
    @Modifying
    @Query(value = "DELETE vp.*,vpr.* FROM publicize as vp LEFT JOIN publicize_report as vpr ON vpr.publicity_id=vp.id WHERE vp.id=:id", nativeQuery = true)
    void removeAllById(long id);

    List<Publicize> findAllByName(String name);
}
