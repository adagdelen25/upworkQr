package com.upwork.hometask.demo.services.iprule;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.exception.BadRequestException;
import com.upwork.hometask.demo.exception.RuleNotFoundException;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IpRuleCheckService {

  private final IpRuleRepository ipRuleRepository;

  private final ConcurrentHashMap<Long, IpRule> ruleConcurrentHashMap = new ConcurrentHashMap<>();

  public void refreshData() {
    List<IpRule> all = ipRuleRepository.findAll();
    for (IpRule ipRule : all) {
      ruleConcurrentHashMap.put(ipRule.getId(), ipRule);
    }
  }

  /*
      it is Deprecated because if there are many ip rules it takes too much time for forLoop
 */
  @Deprecated
  public Boolean executeFromCash(String source, String destination) {
    long sourceValue = 0;
    long destinationValue = 0;
    try {
      sourceValue = IpUtil.ipToLong(source);
      destinationValue = IpUtil.ipToLong(destination);
    } catch (UnknownHostException e) {
      throw new BadRequestException(e.getMessage());
    }
    for (IpRule value : ruleConcurrentHashMap.values()) {
      if (range(value.getDestinationStartValue(), value.getSourceEndValue(), sourceValue)) {
        if (range(value.getDestinationStartValue(), value.getDestinationEndValue(), destinationValue)) {
          return value.getAllow();
        }
      }
    }
    log.error("There is no defined Rule source : " + source + ", end : " + destination);
    throw new RuleNotFoundException("There is no defined Rule source : " + source + ", end : " + destination);
  }

  public Boolean executeFromDb(String source, String destination) {
    long sourceValue = 0;
    long destinationValue = 0;
    try {
      sourceValue = IpUtil.ipToLong(source);
      destinationValue = IpUtil.ipToLong(destination);
    } catch (UnknownHostException e) {
      throw new BadRequestException(e.getMessage());
    }
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

  private static boolean range(Long ipLo, Long ipHi, Long ipToTest) {
    return ipToTest >= ipLo && ipToTest <= ipHi;
  }

  public void addRule(IpRule ipRule) {
    ruleConcurrentHashMap.put(ipRule.getId(), ipRule);
  }

  public void updateRule(IpRule ipRule) {
    ruleConcurrentHashMap.put(ipRule.getId(), ipRule);
  }

  public void removeRule(IpRule ipRule) {
    ruleConcurrentHashMap.remove(ipRule.getId());
  }

}
