package com.upwork.hometask.demo;

import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles(value = "h2")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:~/${random.uuid}"})
@AutoConfigureMockMvc
public class IpRuleRepositoryUnitTest {

    @Autowired
    private IpRuleRepository ipRuleRepository;

    IpRule ipRule;

    Optional<IpRule> optionalIpRule;

    @BeforeEach
    public void setup() {
        ipRule = new IpRule();
        ipRule.setCreatedAt(OffsetDateTime.now());
        ipRule.setCreatedBy("System");
        ipRule.setSpecificName("Test");
        ipRule.setAllow(true);
        ipRule.setSourceStart("192.200.0.0");
        ipRule.setSourceEnd("192.255.0.0");
        ipRule.setDestinationStart("192.200.0.0");
        ipRule.setDestinationEnd("192.255.0.0");
        ipRule.setSourceStartValue(3234332672L);
        ipRule.setSourceEndValue(3237937152L);
        ipRule.setDestinationStartValue(3234332672L);
        ipRule.setDestinationEndValue(3237937152L);

        optionalIpRule = Optional.of(ipRule);
    }


    @Test
    public void testFindById() {
        ipRuleRepository.save(ipRule);
        IpRule result = ipRuleRepository.findById(ipRule.getId()).get();
        Assertions.assertEquals(ipRule.getId(), result.getId());
    }

    @Test
    public void testFindByName() {
        ipRuleRepository.save(ipRule);
        IpRule result = ipRuleRepository.findBySpecificName(ipRule.getSpecificName()).get();
        Assertions.assertEquals(ipRule.getSpecificName(), result.getSpecificName());
        Assertions.assertEquals(ipRule.getId(), result.getId());
    }

    @Test
    public void testFindByRangeValues() {
        ipRuleRepository.save(ipRule);

        Long sourceStartValue = 3234332672L;
        Long sourceEndValue = 3237937152L;
        Long destinationStartValue = 3234332672L;
        Long destinationEndValue = 3237937152L;

        List<IpRule> result = ipRuleRepository.findAllBySourceStartValueAndSourceEndValueAndDestinationStartValueAndDestinationEndValueAndAllow(
                sourceStartValue, sourceEndValue, destinationStartValue, destinationEndValue, true);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 1);

        result = ipRuleRepository.findAllBySourceStartValueAndSourceEndValueAndDestinationStartValueAndDestinationEndValueAndAllow(
                sourceStartValue, sourceEndValue, destinationStartValue, destinationEndValue, null);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 0);

    }


    @Test
    public void listAvailableIpRuleTest() {
        ipRuleRepository.save(ipRule);

        Long sourceStartValue = 3234332673L;
        Long destinationStartValue = 3234332673L;

        List<IpRule> result = ipRuleRepository.listAvailableIpRule(sourceStartValue, destinationStartValue);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 1);
        Assertions.assertEquals(result.get(0).getAllow(), true);


        sourceStartValue = 3234332671L;
        destinationStartValue = 3234332671L;

        result = ipRuleRepository.listAvailableIpRule(sourceStartValue, destinationStartValue);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), 0);

    }

}
