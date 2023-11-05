package com.upwork.hometask.demo.services.synchronize;

import com.upwork.hometask.demo.cache.RangeCacheService;
import com.upwork.hometask.demo.cache.RangeMapCacheService;
import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Deprecated
public class IpRuleSynchronizeCacheService {
    private final IpRuleRepository ipRuleRepository;

    private final RangeCacheService rangeCacheService;
    private final RangeMapCacheService rangeMapCacheService;

    public void refreshData() {
        //todo If you are going to use a cache system, fill this system.

        /*// rangeCacheService
        List<IpRule> all = ipRuleRepository.findAll();
        for (IpRule ipRule : all) {
            rangeCacheService.addIpRule(ipRule);
        }

        // rangeMapCacheService
        for (IpRule ipRule : all) {
            rangeMapCacheService.addIpRule(ipRule);
        }*/

    }
}
