package com.upwork.hometask.demo;

import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.dto.IdOutput;
import com.upwork.hometask.demo.models.exception.AlreadyExistsException;
import com.upwork.hometask.demo.models.exception.BadRequestException;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.resources.iprule.model.InsertInput;
import com.upwork.hometask.demo.resources.iprule.model.UpdateInput;
import com.upwork.hometask.demo.services.iprule.IpRuleCheckService;
import com.upwork.hometask.demo.services.iprule.IpRuleService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IpRuleServiceUnitTest {
    @Mock
    private IpRuleRepository ipRuleRepository;

    @Mock
    private IpRuleCheckService ipRuleCheckService;

    @InjectMocks
    private IpRuleService ipRuleService;

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

    @DisplayName("JUnit test for save ip rule method")
    @Test
    public void givenIpRuleObject_whenSaveIpRule_thenReturnIpRuleId() {

        when(ipRuleRepository.findById(any())).thenReturn(optionalIpRule);
        when(ipRuleRepository.findBySpecificName(any())).thenReturn(Optional.empty());

        InsertInput insertInput = new InsertInput();
        insertInput.setSpecificName("Test1");
        insertInput.setAllow(true);
        insertInput.setSourceStart("192.200.0.0");
        insertInput.setSourceEnd("192.255.0.0");
        insertInput.setDestinationStart("192.200.0.0");
        insertInput.setDestinationEnd("192.255.0.0");
        IdOutput savedRule = ipRuleService.create(insertInput);
        Optional<IpRule> result = ipRuleRepository.findById(savedRule.getId());
        Assertions.assertNotNull(result.get());


        UpdateInput updateInput = new UpdateInput();
        updateInput.setSpecificName("Test1");
        updateInput.setAllow(true);
        updateInput.setSourceStart("192.200.0.0");
        updateInput.setSourceEnd("192.255.0.0");
        updateInput.setDestinationStart("192.200.0.0");
        updateInput.setDestinationEnd("192.255.0.0");
        ipRuleService.update(result.get().getId(), updateInput);
        when(ipRuleRepository.findBySpecificName(any())).thenReturn(optionalIpRule);
        result = ipRuleRepository.findBySpecificName("Test1");
        Assertions.assertNotNull(result.get());
    }


    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenIpRuleObject_whenSaveIpRule_thenThrowsException() {

        when(ipRuleRepository.findBySpecificName(any())).thenReturn(optionalIpRule);
        // when -  action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(AlreadyExistsException.class, () -> {
            InsertInput insertInput = new InsertInput();
            insertInput.setSpecificName("Test1");
            insertInput.setAllow(true);
            insertInput.setSourceStart("192.200.0.0");
            insertInput.setSourceEnd("192.255.0.0");
            insertInput.setDestinationStart("192.200.0.0");
            insertInput.setDestinationEnd("192.255.0.0");
            IdOutput savedRule = ipRuleService.create(insertInput);
        });
    }

}
