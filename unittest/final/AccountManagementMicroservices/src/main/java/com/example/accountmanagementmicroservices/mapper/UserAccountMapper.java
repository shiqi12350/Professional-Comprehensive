package com.example.accountmanagementmicroservices.mapper;

import com.example.accountmanagementmicroservices.entites.dbEntities.AccountInfo;
import com.example.accountmanagementmicroservices.entites.dbEntities.FundAccount;
import com.example.accountmanagementmicroservices.entites.dbEntities.UserInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserAccountMapper {
    /*Select*/
    //userInfo
    @Select("select * from userInfo where userPhone = #{userPhone}")
    List<UserInfo> getUserInfoFromUserPhone(@Param("userPhone")String userPhone);

    @Select("select * from userInfo where userID = #{userID}")
    List<UserInfo> getUserInfoFromUserID(@Param("userID")String userID);

    @Select("select * from userInfo where userPhone = #{userPhone} and password = #{password}")
    List<UserInfo> getUserInfoFromUserPhoneAndPassword(@Param("userPhone")String userPhone,@Param("password")String password);

    //accountInfo
    @Select("select * from accountInfo where userID = #{userID}")
    List<AccountInfo> getAccountInfoFromUserID(@Param("userID")String userID);

    @Select("select * from accountInfo where cardID = #{cardID}")
    List<AccountInfo> getAccountInfoFromCardID(@Param("cardID")String cardID);

    //fundAccount
    @Select("select * from fundAccount where cardID = #{cardID} and fundID = #{fundID}")
    List<FundAccount> getFundAccountFromCardIDAndFundID(@Param("cardID")String cardID, @Param("fundID")String fundID);

    @Select("select * from fundAccount where cardID = #{cardID}")
    List<FundAccount> getFundAccountFromCardID(@Param("cardID")String cardID);


    /*Insert*/
    //userInfo
    @Insert("insert into userInfo values(#{userID},#{userName},#{userPhone},#{password},#{birth},#{email},#{level})")
    void insertUserInfo(@Param("userID")String userID,
                        @Param("userName")String userName,
                        @Param("userPhone")String userPhone,
                        @Param("password")String password,
                        @Param("birth")String birth,
                        @Param("email")String email,
                        @Param("level")int level);

    //accountInfo
    @Insert("insert into accountInfo values(#{cardID},#{userID})")
    void insertAccountInfo(@Param("userID")String userID,
                        @Param("cardID")String cardID);

    //fundAccount
    @Insert("insert into fundAccount values(#{cardID},#{fundID},#{amount})")
    void insertFundAccount(@Param("cardID")String cardID,
                           @Param("fundID")String fundID,
                           @Param("amount")double amount);

    /*Update*/
    //userInfo
    @Update("update userInfo set userName = #{userName} , userPhone = #{userPhone} , password = #{password} , birth = #{birth} , email = #{email} where userID = #{userID}")
    void updateUserInfo(@Param("userID")String userID,
                        @Param("userName")String userName,
                        @Param("userPhone")String userPhone,
                        @Param("password")String password,
                        @Param("birth")String birth,
                        @Param("email")String email);
    @Update("update userInfo set level = #{level}  where userID = #{userID}")
    void updateUserLevel(@Param("userID")String userID,
                        @Param("level")int level);
    //fundAccount
    @Update("update fundAccount set amount = #{amount}  where cardID = #{cardID} and fundID = #{fundID}")
    void updateFundAccount(@Param("cardID")String cardID,
                           @Param("fundID")String fundID,
                           @Param("amount")double amount);
    /*Delete*/
    @Delete("delete from fundAccount where  cardID = #{cardID} and fundID = #{fundID}")
    void deleteFundAccount(@Param("cardID")String cardID,
                           @Param("fundID")String fundID);

    @Delete("delete from accountInfo where  cardID = #{cardID}")
    void deleteAccountInfo(@Param("cardID")String cardID);
}
