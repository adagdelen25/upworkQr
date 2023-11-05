package com.upwork.hometask.demo;

import com.upwork.hometask.demo.models.exception.InvalidIPAddressException;
import com.upwork.hometask.demo.utils.IpUtil;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IpUtilsTest {

    @Test
    public void givenIp_whenCalculate_thenReturnValue() {
        Long actualValue = IpUtil.ipToLong("192.200.0.0");
        Long expectedValue = 3234332672L;
        Assert.assertEquals(actualValue, expectedValue);
    }

    @Test
    public void givenIp_whenCalculate_thenThrowException() {
        Exception exception = assertThrows(InvalidIPAddressException.class, () -> {
            Long actualValue = IpUtil.ipToLong("500.200.0.0");
        });
        String expectedMessage = "No such host is known";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


}
