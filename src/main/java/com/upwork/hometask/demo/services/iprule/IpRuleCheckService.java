package com.upwork.hometask.demo.services.iprule;

import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.exception.RuleNotFoundException;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class IpRuleCheckService {

    private final IpRuleRepository ipRuleRepository;

    public Boolean executeFromDb(String source, String destination) {
        long sourceValue = IpUtil.ipToLong(source);
        long destinationValue = IpUtil.ipToLong(destination);
        List<IpRule> availableIpRules = ipRuleRepository.listAvailableIpRule(sourceValue, destinationValue);
        boolean allow = false;
        boolean deny = false;
        for (IpRule ipRule : availableIpRules) {
            allow = BooleanUtils.isTrue(ipRule.getAllow());
            deny = BooleanUtils.isFalse(ipRule.getAllow());
            if (deny) {
                break;
            }
        }
        if (deny) {
            return false;
        }
        if (allow) {
            return true;
        }
        log.error("There is no defined Rule source : " + source + ", end : " + destination);
        throw new RuleNotFoundException("There is no defined Rule source : " + source + ", end : " + destination);
    }
}
