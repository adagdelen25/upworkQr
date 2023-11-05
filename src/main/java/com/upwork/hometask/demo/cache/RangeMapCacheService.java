package com.upwork.hometask.demo.cache;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.exception.RuleNotFoundException;
import com.upwork.hometask.demo.utils.IpUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Deprecated
@Service
public class RangeMapCacheService {
    RangeMap<Long, RangeMap<Long, Boolean>> rangeMap = TreeRangeMap.create();
    ConcurrentHashMap<Long, Range<Long>> ruleConcurrentHashMap = new ConcurrentHashMap<>();

    public synchronized void addIpRule(IpRule rule) {
        Range<Long> source = Range.closed(rule.getSourceStartValue(), rule.getSourceEndValue());
        RangeMap<Long, Boolean> destRangeMap = TreeRangeMap.create();
        Range<Long> destination = Range.closed(rule.getDestinationStartValue(), rule.getDestinationEndValue());
        destRangeMap.put(destination, rule.getAllow());
        rangeMap.put(source, destRangeMap);
        ruleConcurrentHashMap.put(rule.getId(), source);
    }

    public synchronized void updateIpRule(IpRule rule) {
        Range<Long> source = Range.closed(rule.getSourceStartValue(), rule.getSourceEndValue());
        RangeMap<Long, Boolean> destRangeMap = TreeRangeMap.create();
        Range<Long> destination = Range.closed(rule.getDestinationStartValue(), rule.getDestinationEndValue());
        destRangeMap.put(destination, rule.getAllow());

        Range<Long> oldSourceMap = ruleConcurrentHashMap.get(rule.getId());
        rangeMap.remove(oldSourceMap);
        rangeMap.put(source, destRangeMap);
        ruleConcurrentHashMap.put(rule.getId(), source);
    }

    public synchronized void removeIpRule(IpRule rule) {
        Range<Long> oldSourceMap = ruleConcurrentHashMap.get(rule.getId());
        rangeMap.remove(oldSourceMap);
        ruleConcurrentHashMap.remove(rule.getId(), oldSourceMap);
    }

    public Boolean getResult(String source, String destination) {
        long sourceValue = IpUtil.ipToLong(source);
        long destinationValue = IpUtil.ipToLong(destination);
        RangeMap<Long, Boolean> destRangeMap = rangeMap.get(sourceValue);
        if (destRangeMap == null) {
            throw new RuleNotFoundException("There is no defined Rule source : " + source + ", end : " + destination);
        }
        Boolean check = destRangeMap.get(destinationValue);
        if (check == null) {
            throw new RuleNotFoundException("There is no defined Rule source : " + source + ", end : " + destination);
        }
        return check;
    }

    public Boolean getResult(long sourceValue, long destinationValue) {
        RangeMap<Long, Boolean> destRangeMap = rangeMap.get(sourceValue);
        if (destRangeMap == null) {
            throw new RuleNotFoundException("There is no defined Rule source : " + sourceValue + ", end : " + destinationValue);
        }
        Boolean check = destRangeMap.get(destinationValue);
        if (check == null) {
            throw new RuleNotFoundException("There is no defined Rule source : " + sourceValue + ", end : " + destinationValue);
        }
        return check;
    }
}
