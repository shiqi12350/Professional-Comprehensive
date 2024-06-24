package com.example.trademanagementmicroservice;


import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.example.trademanagementmicroservice.services.Trade;



@SpringBootTest
@Transactional
@Rollback
class TradeManagementTest {

  @Autowired
    private Trade Trade;


    @BeforeEach
    void setUp() {
        System.out.println("测试开始:");

    }

    @AfterEach
    void tearDown() {
        System.out.println("测试结束!");
    }
   
    @Test
    @DisplayName("测试extractFirstSixDigits方法，参数为空")
    void UT_TC_002_001_001_001() {
        String fundID = "";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Trade.extractFirstSixDigits(fundID);
        });
        // Verifying the result
        assertEquals("fundID cannot be empty", exception.getMessage());
    }


    @Test
    @DisplayName("测试extractFirstSixDigits方法，基金代码不存在")
    void UT_TC_002_001_002_001() {
        String fundID = "123";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Trade.extractFirstSixDigits(fundID);
        });
        // Verifying the result
        assertEquals("fundID must be at least 6 characters long", exception.getMessage());
    }

    @Test
    @DisplayName("测试extractFirstSixDigits方法，各参数合法")
    void UT_TC_002_001_003_001() {
        String fundID = "123456";

        assertEquals("123456", Trade.extractFirstSixDigits(fundID));
    }


    @Test
    @DisplayName("测试extractFirstSixDigits方法，各参数合法")
    void UT_TC_002_001_004_001() {
        String fundID = "123456789";

        assertEquals("123456", Trade.extractFirstSixDigits(fundID));
    }
   
}
