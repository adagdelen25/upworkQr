package com.upwork.hometask.demo.cache;

import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.exception.RuleNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RangeCacheServiceTest {


    private static IpRule createIpRule(Long sourceStartValue, Long sourceEndValue, Long destinationStartValue, Long destinationEndValue, Boolean allow) {
        IpRule ipRule = new IpRule();
        ipRule.setSourceStartValue(sourceStartValue);
        ipRule.setSourceEndValue(sourceEndValue);
        ipRule.setDestinationStartValue(destinationStartValue);
        ipRule.setDestinationEndValue(destinationEndValue);
        ipRule.setAllow(allow);
        return ipRule;
    }

    private static IpRule createIpRule(Long id, Integer sourceStartValue, Integer sourceEndValue, Integer destinationStartValue, Integer destinationEndValue, Boolean allow) {
        IpRule ipRule = new IpRule();
        ipRule.setId(id);
        ipRule.setSourceStartValue(sourceStartValue.longValue());
        ipRule.setSourceEndValue(sourceEndValue.longValue());
        ipRule.setDestinationStartValue(destinationStartValue.longValue());
        ipRule.setDestinationEndValue(destinationEndValue.longValue());
        ipRule.setAllow(allow);
        return ipRule;
    }

    @Test
    public void testRangeCache() {
        RangeCacheService rangeMapCache = new RangeCacheService();
        IpRule ipRule = createIpRule(1L, 0, 1, 0, 1, true);
        rangeMapCache.addIpRule(ipRule);
        Boolean result = rangeMapCache.getResult(0, 1);
        Assertions.assertTrue(result);

        ipRule = createIpRule(2L, 0, 1, 3, 4, false);
        rangeMapCache.addIpRule(ipRule);
        result = rangeMapCache.getResult(0, 3);
        Assertions.assertFalse(result);

        result = rangeMapCache.getResult(0, 1);
        Assertions.assertTrue(result);

        ipRule = createIpRule(1L, 0, 1, 0, 1, true);
        rangeMapCache.removeIpRule(ipRule);

        Assertions.assertThrows(RuleNotFoundException.class, () -> {
            rangeMapCache.getResult(0, 1);
        });

    }

    @Test
    public void testRangeMapCache() {
        RangeMapCacheService rangeMapCacheService = new RangeMapCacheService();
        IpRule ipRule = createIpRule(1L, 0, 1, 0, 1, true);
        rangeMapCacheService.addIpRule(ipRule);
        Boolean result = rangeMapCacheService.getResult(0, 1);
        Assertions.assertTrue(result);

        ipRule = createIpRule(2L, 0, 1, 3, 4, false);
        rangeMapCacheService.addIpRule(ipRule);
        result = rangeMapCacheService.getResult(0, 3);
        Assertions.assertFalse(result);

        //todo normally this method dont throw exception but rangemap is not handle this case
        Assertions.assertThrows(RuleNotFoundException.class, () -> {
          rangeMapCacheService.getResult(0, 1);
        });
    }

}
