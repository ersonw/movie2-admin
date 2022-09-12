package com.example.movie2admin.dao;

import com.example.movie2admin.entity.DiamondOrder;
import com.example.movie2admin.entity.GameOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface DiamondOrderDao extends JpaRepository<DiamondOrder, Long>, CrudRepository<DiamondOrder, Long> {
    DiamondOrder findAllByOrderNo(String orderId);

    Page<DiamondOrder> findAllByOrderNoLike(String s, Pageable pageable);
}
