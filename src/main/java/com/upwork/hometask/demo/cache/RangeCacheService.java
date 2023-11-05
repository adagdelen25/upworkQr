package com.upwork.hometask.demo.cache;

import com.google.common.collect.Range;
import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.exception.RuleNotFoundException;
import com.upwork.hometask.demo.utils.IpUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Deprecated
public class RangeCacheService {
    Collection<Option> denies = Collections.synchronizedCollection(new ArrayList<>());
    Collection<Option> allows = Collections.synchronizedCollection(new ArrayList<>());
    ConcurrentHashMap<Long, Option> optionMap = new ConcurrentHashMap<>();

    public void addIpRule(IpRule rule) {
        Option option = new Option(rule.getAllow(),
                Range.closed(rule.getSourceStartValue(), rule.getSourceEndValue()),
                Range.closed(rule.getDestinationStartValue(), rule.getDestinationEndValue())
        );
        if (Boolean.TRUE.equals(rule.getAllow())) {
            allows.add(option);
        } else {
            denies.add(option);
        }
        optionMap.put(rule.getId(), option);
    }

    public void updateIpRule(IpRule rule) {
        Option option = new Option(rule.getAllow(),
                Range.closed(rule.getSourceStartValue(), rule.getSourceEndValue()),
                Range.closed(rule.getDestinationStartValue(), rule.getDestinationEndValue())
        );
        Option optionOld = optionMap.get(rule.getId());
        if (!Objects.equals(optionOld.allow, option.allow)) {
            if (Boolean.TRUE.equals(option.allow)) {
                denies.remove(optionOld);
                allows.add(option);
            } else {
                allows.remove(optionOld);
                denies.add(option);
            }
        }
        optionMap.put(rule.getId(), option);
    }

    public void removeIpRule(IpRule rule) {
        Option optionOld = optionMap.get(rule.getId());
        if (Boolean.TRUE.equals(optionOld.allow)) {
            allows.remove(optionOld);
        } else {
            denies.remove(optionOld);
        }
        optionMap.remove(optionOld);
    }

    public Boolean getResult(String source, String destination) {
        long sourceValue = IpUtil.ipToLong(source);
        long destinationValue = IpUtil.ipToLong(destination);
        for (Option deny : denies) {
            if (deny.source.contains(sourceValue) && deny.destination.contains(destinationValue)) {
                return false;
            }
        }
        for (Option allow : allows) {
            if (allow.source.contains(sourceValue) && allow.destination.contains(destinationValue)) {
                return true;
            }
        }
        throw new RuleNotFoundException("There is no defined Rule source : " + source + ", end : " + destination);
    }

    public Boolean getResult(long sourceValue, long destinationValue) {
        for (Option deny : denies) {
            if (deny.source.contains(sourceValue) && deny.destination.contains(destinationValue)) {
                return false;
            }
        }
        for (Option allow : allows) {
            if (allow.source.contains(sourceValue) && allow.destination.contains(destinationValue)) {
                return true;
            }
        }
        throw new RuleNotFoundException("There is no defined Rule source : " + sourceValue + ", end : " + destinationValue);
    }

    @AllArgsConstructor
    public static class Option {
        Boolean allow;
        Range<Long> source;
        Range<Long> destination;
    }
}
