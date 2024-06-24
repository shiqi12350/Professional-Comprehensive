package com.example.informationmaintenancemicroservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.informationmaintenancemicroservice.entites.ReturnInfo;
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

import com.example.informationmaintenancemicroservice.entites.ReturnValue;
import com.example.informationmaintenancemicroservice.entites.SubscriptionFee;
import com.example.informationmaintenancemicroservice.mapper.InfoMapper;
import com.example.informationmaintenancemicroservice.services.BasicServices;

@SpringBootTest
@Transactional
@Rollback
class InformationMaintenanceMicroserviceApplicationTests {

    @Autowired
    private BasicServices BasicServices;

    @MockBean
    private InfoMapper InfoMapper;

    private SubscriptionFee testExistingUser;
    private List<Double> valueList;
    private List<String> infoList;

    // private MessageNotificationFeignServices messageNotificationFeignServices;

    @BeforeEach
    void setUp() {
        System.out.println("测试开始:");
        // 初始化预定义的正确用例
        testExistingUser = new SubscriptionFee();
        testExistingUser.setValue(0.005);
        testExistingUser.setFundCode("123");

        valueList = new ArrayList<>();
        valueList.add(0.005);

        infoList = new ArrayList<>();
        infoList.add("fund123info");
    }

    @AfterEach
    void tearDown() {
        System.out.println("测试结束!");
    }

    private void mockgetManageFee(String fundCode) {
        when(InfoMapper.getManageFee(fundCode))
                .thenReturn(fundCode.equals(testExistingUser.getFundCode())
                        ? valueList
                        : Collections.emptyList());
    }

    private void mockgetCustodyFee(String fundCode) {
        when(InfoMapper.getCustodyFee(fundCode))
                .thenReturn(fundCode.equals(testExistingUser.getFundCode())
                        ? valueList
                        : Collections.emptyList());
    }

    private void  mockGetFundInfo(String fundCode) {
        when(InfoMapper.getFundInfo(fundCode))
                .thenReturn(fundCode.equals(testExistingUser.getFundCode())
                        ? infoList
                        : Collections.emptyList());
    }

    @Test
    @DisplayName("测试getManageFee方法，参数为空")
    void UT_TC_003_001_001_001() {
        String fundCode = "";

        mockgetManageFee(fundCode);

        ReturnValue result = BasicServices.getManageFee(fundCode);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试getManageFee方法，基金代码不存在")
    void UT_TC_003_001_002_001() {
        String fundCode = "999999";

        mockgetManageFee(fundCode);

        ReturnValue result = BasicServices.getManageFee(fundCode);

        // Verifying the result
        assertEquals(-1, result.getResultCode());
    }

    @Test
    @DisplayName("测试getManageFee方法，各参数合法")
    void UT_TC_003_001_003_001() {
        String fundCode = "123";

        mockgetManageFee(fundCode);

        ReturnValue result = BasicServices.getManageFee(fundCode);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals(0.005, result.getValue(), 0.0001); // 使用delta以处理double比较的精度问题
    }

    @Test
    @DisplayName("测试getCustodyFee方法，参数为空")
    void UT_TC_003_002_001_001() {
        String fundCode = "";

        mockgetCustodyFee(fundCode);

        ReturnValue result = BasicServices.getCustodyFee(fundCode);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试getCustodyFee方法，基金代码不存在")
    void UT_TC_003_002_002_001() {
        String fundCode = "999999";

        mockgetCustodyFee(fundCode);

        ReturnValue result = BasicServices.getCustodyFee(fundCode);

        // Verifying the result
        assertEquals(-1, result.getResultCode());
    }

    @Test
    @DisplayName("测试getCustodyFee方法，各参数合法")
    void UT_TC_003_002_003_001() {
        String fundCode = "123";

        mockgetCustodyFee(fundCode);

        ReturnValue result = BasicServices.getCustodyFee(fundCode);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals(0.005, result.getValue(), 0.0001); // 使用delta以处理double比较的精度问题
    }

    @Test
    @DisplayName("测试getFundInfo方法，参数为空")
    void UT_TC_003_003_001_001() {
        String fundCode = "";

        mockGetFundInfo(fundCode);

        ReturnInfo result = BasicServices.getFundInfo(fundCode);

        // Verifying the result
        assertEquals(-101, result.getResultCode());
    }

    @Test
    @DisplayName("测试getFundInfo方法，基金代码不存在")
    void UT_TC_003_003_002_001() {
        String fundCode = "999999";

        mockGetFundInfo(fundCode);

        ReturnInfo result = BasicServices.getFundInfo(fundCode);

        // Verifying the result
        assertEquals(-1, result.getResultCode());
    }

    @Test
    @DisplayName("测试GetFundInfo方法，各参数合法")
    void UT_TC_003_003_003_001() {
        String fundCode = "123";

        mockGetFundInfo(fundCode);

        ReturnInfo result = BasicServices.getFundInfo(fundCode);

        // Verifying the result
        assertEquals(1, result.getResultCode());
        assertEquals("fund123info", result.getInfo());
    }

}
