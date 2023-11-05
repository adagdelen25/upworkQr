package com.upwork.hometask.demo;

import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.exception.RuleNotFoundException;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.services.iprule.IpRuleCheckService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IpRuleCheckServiceUnitTest {
    @Mock
    private IpRuleRepository ipRuleRepository;

    @InjectMocks
    private IpRuleCheckService ipRuleCheckService;

    IpRule ipRuleAllow;
    IpRule ipRuleDelay;

    Optional<IpRule> optionalIpRule;

    @BeforeEach
    public void setup() {
        ipRuleAllow = new IpRule();
        ipRuleAllow.setCreatedAt(OffsetDateTime.now());
        ipRuleAllow.setCreatedBy("System");
        ipRuleAllow.setSpecificName("Test");
        ipRuleAllow.setAllow(true);
        ipRuleAllow.setSourceStart("192.200.0.0");
        ipRuleAllow.setSourceEnd("192.255.0.0");
        ipRuleAllow.setDestinationStart("192.200.0.0");
        ipRuleAllow.setDestinationEnd("192.255.0.0");
        ipRuleAllow.setSourceStartValue(3234332672L);
        ipRuleAllow.setSourceEndValue(3237937152L);
        ipRuleAllow.setDestinationStartValue(3234332672L);
        ipRuleAllow.setDestinationEndValue(3237937152L);


        ipRuleDelay = new IpRule();
        ipRuleDelay.setCreatedAt(OffsetDateTime.now());
        ipRuleDelay.setCreatedBy("System");
        ipRuleDelay.setSpecificName("Test");
        ipRuleDelay.setAllow(false);
        ipRuleDelay.setSourceStart("192.200.0.0");
        ipRuleDelay.setSourceEnd("192.255.0.0");
        ipRuleDelay.setDestinationStart("192.200.0.0");
        ipRuleDelay.setDestinationEnd("192.255.0.0");
        ipRuleDelay.setSourceStartValue(3234332672L);
        ipRuleDelay.setSourceEndValue(3237937152L);
        ipRuleDelay.setDestinationStartValue(3234332672L);
        ipRuleDelay.setDestinationEndValue(3237937152L);

        optionalIpRule = Optional.of(ipRuleAllow);
    }

    @DisplayName("JUnit test for save ip rule method")
    @Test
    public void givenIpToIp_whenCheckIpInterval_returnTrue() {
        when(ipRuleRepository.listAvailableIpRule(any(), any())).thenReturn(List.of(ipRuleAllow));
        Boolean check = ipRuleCheckService.executeFromDb("192.100.3.0", "192.150.3.0");
        Assertions.assertTrue(check);
    }

    @DisplayName("JUnit test for save ip rule method")
    @Test
    public void givenIpToIp_whenCheckIpInterval_returnFalse() {
        when(ipRuleRepository.listAvailableIpRule(any(), any())).thenReturn(List.of(ipRuleAllow, ipRuleDelay));
        Boolean check = ipRuleCheckService.executeFromDb("192.100.3.0", "192.150.3.0");
        Assertions.assertFalse(check);
    }

    @DisplayName("JUnit test for save ip rule method")
    @Test
    public void givenIpToIp_whenCheckIpInterval_returnRuleNotFoundException() {
        when(ipRuleRepository.listAvailableIpRule(any(), any())).thenReturn(List.of());
        Exception exception = assertThrows(RuleNotFoundException.class, () -> {
            ipRuleCheckService.executeFromDb("191.100.3.0", "191.150.3.0");
        });
    }
}
