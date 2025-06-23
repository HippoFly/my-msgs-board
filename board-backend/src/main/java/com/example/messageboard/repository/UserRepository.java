package com.example.messageboard.repository;

import com.example.messageboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 用户数据访问接口
 * 提供用户相关的数据库操作方法，继承自JpaRepository以获得基本的CRUD功能
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象，如果不存在则返回null
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查找用户
     * @param email 邮箱地址
     * @return 用户对象，如果不存在则返回null
     */
    User findByEmail(String email);
}
