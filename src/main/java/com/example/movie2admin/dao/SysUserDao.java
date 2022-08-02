package com.example.movie2admin.dao;

import com.example.movie2admin.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SysUserDao extends JpaRepository<SysUser, Long>, CrudRepository<SysUser, Long> {
    SysUser findAllById(long id);
    SysUser findAllByUsername(String username);
}
