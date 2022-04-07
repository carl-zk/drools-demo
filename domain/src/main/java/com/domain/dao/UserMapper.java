package com.domain.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.domain.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author carl
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM USER WHERE AGE=#{age} AND GENDER=${gender} AND DELETED=0")
    List<User> findAllByAgeAndGenderUsers(Integer age, String gender);
}
