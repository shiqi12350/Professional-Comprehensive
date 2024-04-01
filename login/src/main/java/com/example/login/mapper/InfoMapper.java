package com.example.login.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InfoMapper {
    @Select("select userName from LoginAndSign where userName =#{userName}")
    List<String> checkUserNameExist(@Param("userName")String userName);

    @Select("select userName from LoginAndSign where userName =#{userName} and passWord = #{passWord}")
    List<String> checkPassWordCorrect(@Param("userName")String userName,@Param("passWord")String passWord);

    @Insert("insert into LoginAndSign values(#{userName},#{passWord})")
    void insertNewAccount(@Param("userName")String userName,@Param("passWord")String passWord);
}
