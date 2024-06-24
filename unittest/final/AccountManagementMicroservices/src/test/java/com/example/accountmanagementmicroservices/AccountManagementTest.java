package com.example.accountmanagementmicroservices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.accountmanagementmicroservices.entites.dbEntities.AccountInfo;
import com.example.accountmanagementmicroservices.entites.dbEntities.UserInfo;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnBankCardList;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnUserEmail;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnUserID;
import com.example.accountmanagementmicroservices.entites.returnEntites.AMMReturnUserInfo;
import com.example.accountmanagementmicroservices.mapper.UserAccountMapper;
import com.example.accountmanagementmicroservices.services.UserServices;
import com.example.accountmanagementmicroservices.userFeign.MessageNotificationFeignServices;

@SpringBootTest
@Transactional
@Rollback
class AccountManagementTest {

    @Autowired
    private UserServices userServices;

    @MockBean
    private UserAccountMapper userAccountMapper;

    private UserInfo testExistingUser;
    private List<AccountInfo> testExistingcard;

    private MessageNotificationFeignServices messageNotificationFeignServices;


    @BeforeEach
    void setUp() {
        System.out.println("测试开始:");
        // 初始化预定义的正确用例
        testExistingUser = new UserInfo();
        testExistingUser.setUserID("9876543210");
        testExistingUser.setUserPhone("13656666666");
        testExistingUser.setPassword("123456");
        testExistingUser.setUserName("xiaoming");
        testExistingUser.setBirth("2000.1");
        testExistingUser.setEmail("1234567898@qq.com");

        testExistingcard = new ArrayList<>();
        testExistingcard.add(new AccountInfo("card9876", "9876543210"));
        testExistingcard.add(new AccountInfo("card5432", "9876543210"));

    }

    @AfterEach
    void tearDown() {
        System.out.println("测试结束!");
    }

    private void mockUserAccountMapper(String userPhone, String password) {
        when(userAccountMapper.getUserInfoFromUserPhoneAndPassword(userPhone, password))
                .thenReturn(userPhone.equals(testExistingUser.getUserPhone()) && password.equals(testExistingUser.getPassword())
                        ? Collections.singletonList(testExistingUser)
                        : Collections.emptyList());
    }

    private void mockgetUserEmailFromCardID(String cardId) {
        when(userAccountMapper. getAccountInfoFromCardID(cardId))
                .thenReturn(cardId.equals(testExistingcard.get(0).getCardID())||cardId.equals(testExistingcard.get(1).getCardID())
                        ? testExistingcard
                        : Collections.emptyList());

        when(userAccountMapper.getUserInfoFromUserID(testExistingcard.get(0).getUserID()))
                .thenReturn(Collections.singletonList(testExistingUser));
    }

    private void mockgetBankCardList(String userId) {
        when(userAccountMapper.getUserInfoFromUserID(userId))
                .thenReturn(userId.equals(testExistingUser.getUserID())
                        ? Collections.singletonList(testExistingUser)
                        : Collections.emptyList());

        when(userAccountMapper.getAccountInfoFromUserID(userId)).thenReturn(testExistingcard);
    }

    private void  mockGetUserInfo(String userId) {
        when(userAccountMapper.getUserInfoFromUserID(userId))
                .thenReturn(userId.equals(testExistingUser.getUserID())
                        ? Collections.singletonList(testExistingUser)
                        : Collections.emptyList());
    }

    private void   mockgetUserEmail(String userId) {
        when(userAccountMapper.getUserInfoFromUserID(userId))
                .thenReturn(userId.equals(testExistingUser.getUserID())
                        ? Collections.singletonList(testExistingUser)
                        : Collections.emptyList());
    }

    @Test
    @DisplayName("测试Login方法，手机号为空")
    void UT_TC_001_001_001_001() {
        String userPhone = "";
        String password = "123456";

        mockUserAccountMapper(userPhone, password);

        AMMReturnUserID result = userServices.Login(userPhone, password);

        // Verifying the result
        assertEquals(-102, result.getResultCode());
    }

    @Test
    @DisplayName("测试Login方法，密码为空")
    void UT_TC_001_001_001_002() {
        String userPhone = "13656666666";
        String password = "";

        mockUserAccountMapper(userPhone, password);

        AMMReturnUserID result = userServices.Login(userPhone, password);

        // Verifying the result
        assertEquals(-103, result.getResultCode());
    }

    @Test
    @DisplayName("测试Login方法，手机号与密码不匹配")
    void UT_TC_001_001_002_001() {
        String userPhone = "13656666666";
        String password = "000000";

        mockUserAccountMapper(userPhone, password);

        AMMReturnUserID result = userServices.Login(userPhone, password);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试Login方法，各参数合法")
    void UT_TC_001_001_003_001() {
        String userPhone = "13656666666";
        String password = "123456";

        mockUserAccountMapper(userPhone, password);

        AMMReturnUserID result = userServices.Login(userPhone, password);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals("9876543210", result.getUserID());
    }


    @Test
    @DisplayName("测试getUserEmailFromCardID方法，cardID为空")
    void UT_TC_001_002_001_001() {
        String userID = "";
        mockgetUserEmailFromCardID(userID);
        AMMReturnUserEmail result = userServices.getUserEmailFromCardID(userID);

        // Verifying the result
        assertEquals(-104, result.getResultCode());
    }

    @Test
    @DisplayName("测试getUserEmailFromCardID方法，cardID不存在")
    void UT_TC_001_002_002_001() {
        String userID = "6666666666";
        mockgetUserEmailFromCardID(userID);
        AMMReturnUserEmail result = userServices.getUserEmailFromCardID(userID);

        // Verifying the result
        assertEquals(-104, result.getResultCode());
    }

    @Test
    @DisplayName("测试getUserEmailFromCardID方法，参数合法")
    void UT_TC_001_002_003_001() {
        String userID = "card9876";
        mockgetUserEmailFromCardID(userID);
        AMMReturnUserEmail result = userServices.getUserEmailFromCardID(userID);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals("1234567898@qq.com", result.getEmail());
    }


    @Test
    @DisplayName("测试getBankCardList方法，用户ID为空")
    void UT_TC_001_003_001_001() {
        String userID = "";
        mockgetBankCardList(userID);
        AMMReturnBankCardList result = userServices.getBankCardList(userID);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试getBankCardList方法，用户ID不存在")
    void UT_TC_001_003_002_001() {
        String userID = "6666666666";
        mockgetBankCardList(userID);
        AMMReturnBankCardList result = userServices.getBankCardList(userID);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试getBankCardList方法，参数合法")
    void UT_TC_001_003_003_001() {
        String userID = "9876543210";
        mockgetBankCardList(userID);
        AMMReturnBankCardList result = userServices.getBankCardList(userID);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals("card9876", result.getBankCardList().get(0));
        assertEquals("card5432", result.getBankCardList().get(1));
    }



    @Test
    @DisplayName("测试GetUserInfo方法，用户ID为空")
    void UT_TC_001_004_001_001() {
        String userID = "";
        mockGetUserInfo(userID);
        AMMReturnUserInfo result = userServices.GetUserInfo(userID);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试GetUserInfo方法，用户ID不存在")
    void UT_TC_001_004_002_001() {
        String userID = "888888";
        mockGetUserInfo(userID);
        AMMReturnUserInfo result = userServices.GetUserInfo(userID);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试GetUserInfo方法，参数合法")
    void UT_TC_001_004_003_001() {
        String userID = "9876543210";
        mockGetUserInfo(userID);
        AMMReturnUserInfo result = userServices.GetUserInfo(userID);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals("9876543210", result.getUserID());
    }


    @Test
    @DisplayName("测试getUserEmail方法，用户ID为空")
    void UT_TC_001_005_001_001() {
        String userID = "";
        mockgetUserEmail(userID);
        AMMReturnUserEmail result = userServices.getUserEmail(userID);

        // Verifying the result
        assertEquals(-102, result.getResultCode());
    }

    @Test
    @DisplayName("测试getUserEmail方法，用户ID不存在")
    void UT_TC_001_005_002_001() {
        String userID = "888888";
        mockgetUserEmail(userID);
        AMMReturnUserEmail result = userServices.getUserEmail(userID);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试getUserEmail方法，参数合法")
    void UT_TC_001_005_003_001() {
        String userID = "9876543210";
        mockgetUserEmail(userID);
        AMMReturnUserEmail result = userServices.getUserEmail(userID);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals("1234567898@qq.com", result.getEmail());
    }


}


