package com.example.messageboard.repository;

import com.example.messageboard.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 留言数据访问接口
 * 提供留言的数据库操作方法，继承自JpaRepository以获得基本的CRUD功能
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    /**
     * 查找指定用户的所有可见留言，按时间倒序排序
     * @param user 用户对象
     * @return 留言列表
     */
    List<Message> findByAuthorAndVisibleTrueOrderByTimeDesc(com.example.messageboard.entity.User user);

    /**
     * 查找所有匿名且可见的留言，按时间倒序排序
     * @return 留言列表
     */
    List<Message> findByAuthorIsNullAndVisibleTrueOrderByTimeDesc();

    /**
     * 查找所有可见的留言，按时间倒序排序
     * @return 留言列表
     */
    List<Message> findByVisibleTrueOrderByTimeDesc();

    List<Message> findAllByOrderByTimeDesc();
}
