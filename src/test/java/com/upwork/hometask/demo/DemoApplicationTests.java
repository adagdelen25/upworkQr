package com.upwork.hometask.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import javax.persistence.EntityNotFoundException;
import java.net.UnknownHostException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.exception.BadRequestException;
import com.upwork.hometask.demo.exception.RuleNotFoundException;
import com.upwork.hometask.demo.models.resp.VgpResponse;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.resources.iprule.IpRuleController;
import com.upwork.hometask.demo.resources.iprule.model.CheckInput;
import com.upwork.hometask.demo.resources.iprule.model.InsertInput;
import com.upwork.hometask.demo.resources.iprule.model.InsertSubnetInput;
import com.upwork.hometask.demo.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles(value = "h2")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:~/${random.uuid}"
})
@AutoConfigureMockMvc
class DemoApplicationTests {

  @Autowired
  protected MockMvc mvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Autowired
  protected IpRuleRepository ipRuleRepository;

  @Autowired
  protected IpRuleController ipRuleController;

  private final static String basePath = "/api/iprule";

  private void testGet(String path, String params) throws Exception {
    mvc.perform(get(basePath + path + params)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  protected MvcResult testPost(String path, Object content) throws Exception {
    if (content != null) {
      return mvc.perform(
          post(basePath + path)
              .content(objectMapper.writeValueAsString(content))
              .contentType(MediaType.APPLICATION_JSON)).andReturn();
    } else {
      return mvc.perform(
          post(basePath + path)
              .contentType(MediaType.APPLICATION_JSON)).andReturn();
    }
  }

  protected void testDelete(String path, String params) throws Exception {
    mvc.perform(delete(basePath + path + params)
        .content(params)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  public static void main(String[] args) {
    try {
      System.out.println("192.200.0.0 : " + IpUtil.ipToLong("192.200.0.0"));
      System.out.println("192.255.0.0 : " + IpUtil.ipToLong("192.255.0.0"));
      System.out.println("165.72.64.1 : " + IpUtil.ipToLong("165.72.64.1"));
      System.out.println("165.72.95.254 : " + IpUtil.ipToLong("165.72.95.254"));
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  @Test
  void addIPv4Addresses_Range() throws Exception {
    InsertInput insertInput = new InsertInput();
    insertInput.setSpecificName("Test1");
    insertInput.setAllow(true);
    insertInput.setSourceStart("192.200.0.0");
    insertInput.setSourceEnd("192.255.0.0");
    insertInput.setDestinationStart("192.200.0.0");
    insertInput.setDestinationEnd("192.255.0.0");

    MvcResult mvcResult = testPost("", insertInput);

    IpRule ipRule =
        ipRuleRepository
            .findById(1L)
            .orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + 1L));

    Assertions.assertEquals(ipRule.getSpecificName(), "Test1");
    Assertions.assertEquals(ipRule.getAllow(), true);
    Assertions.assertEquals(ipRule.getSourceStart(), "192.200.0.0");
    Assertions.assertEquals(ipRule.getSourceEnd(), "192.255.0.0");
    Assertions.assertEquals(ipRule.getDestinationStart(), "192.200.0.0");
    Assertions.assertEquals(ipRule.getDestinationEnd(), "192.255.0.0");
    Assertions.assertEquals(ipRule.getSourceStartValue(), 3234332672L);
    Assertions.assertEquals(ipRule.getSourceEndValue(), 3237937152L);
    Assertions.assertEquals(ipRule.getDestinationStartValue(), 3234332672L);
    Assertions.assertEquals(ipRule.getDestinationEndValue(), 3237937152L);

    Assertions.assertThrows(BadRequestException.class, () -> {
      ipRuleController.create(null,insertInput);
    });
    Assertions.assertThrows(BadRequestException.class, () -> {
      insertInput.setSpecificName("Test2");
      ipRuleController.create(null,insertInput);
    });
    Assertions.assertThrows(BadRequestException.class, () -> {
      insertInput.setSpecificName("Test2");
      ipRuleController.create(null,insertInput);
    });


  }


  @Test
  void addIPv4Addresses_Subnet_Range() throws Exception {
    InsertSubnetInput insertInput = new InsertSubnetInput();
    insertInput.setSpecificName("Test1");
    insertInput.setAllow(true);
    insertInput.setSubnetSource("165.72.83.194/19");
    insertInput.setSubnetDestination("165.72.83.194/19");

    MvcResult mvcResult = testPost("/subnet", insertInput);

    IpRule ipRule =
        ipRuleRepository
            .findById(1L)
            .orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + 1L));

    Assertions.assertEquals(ipRule.getSpecificName(), "Test1");
    Assertions.assertEquals(ipRule.getAllow(), true);
    Assertions.assertEquals(ipRule.getIsSubnet(), true);
    Assertions.assertEquals(ipRule.getSubnetSource(), "165.72.83.194/19");
    Assertions.assertEquals(ipRule.getSubnetDestination(), "165.72.83.194/19");
    Assertions.assertEquals(ipRule.getSourceStart(), "165.72.64.1");
    Assertions.assertEquals(ipRule.getSourceEnd(), "165.72.95.254");
    Assertions.assertEquals(ipRule.getDestinationStart(), "165.72.64.1");
    Assertions.assertEquals(ipRule.getDestinationEnd(), "165.72.95.254");
    Assertions.assertEquals(ipRule.getSourceStartValue(), 2772975617L);
    Assertions.assertEquals(ipRule.getSourceEndValue(), 2772983806L);
    Assertions.assertEquals(ipRule.getDestinationStartValue(), 2772975617L);
    Assertions.assertEquals(ipRule.getDestinationEndValue(), 2772983806L);

  }

  @Test
  void deleteIPv4Addresses_Range() throws Exception {
    InsertInput insertInput = new InsertInput();
    insertInput.setSpecificName("Test1");
    insertInput.setAllow(true);
    insertInput.setSourceStart("192.200.0.0");
    insertInput.setSourceEnd("192.255.0.0");
    insertInput.setDestinationStart("192.200.0.0");
    insertInput.setDestinationEnd("192.255.0.0");

    MvcResult mvcResult = testPost("", insertInput);

    IpRule ipRule =
        ipRuleRepository
            .findById(1L)
            .orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + 1L));

    testDelete("/1", "");

    Assertions.assertThrows(RuleNotFoundException.class, () -> {
      CheckInput checkInput2 = new CheckInput();
      checkInput2.setSource("192.200.3.0");
      checkInput2.setDestination("192.200.3.0");
      ipRuleController.check(checkInput2);
    });

  }

  @Test
  void checkIpRule() throws Exception {

    testGet("", "");

    InsertInput insertInput = new InsertInput();
    insertInput.setSpecificName("Test1");
    insertInput.setAllow(true);
    insertInput.setSourceStart("192.200.0.0");
    insertInput.setSourceEnd("192.255.0.0");
    insertInput.setDestinationStart("192.200.0.0");
    insertInput.setDestinationEnd("192.255.0.0");

    MvcResult mvcResult = testPost("", insertInput);

    CheckInput checkInput = new CheckInput();
    checkInput.setSource("192.200.3.0");
    checkInput.setDestination("192.200.3.0");
    VgpResponse<Boolean> check = ipRuleController.check(checkInput);
    Assertions.assertEquals(check.getBody().getData(), true);

    Assertions.assertThrows(RuleNotFoundException.class, () -> {
      CheckInput checkInput2 = new CheckInput();
      checkInput2.setSource("192.100.3.0");
      checkInput2.setDestination("192.150.3.0");
      ipRuleController.check(checkInput2);
    });

    insertInput = new InsertInput();
    insertInput.setSpecificName("Test2");
    insertInput.setAllow(false);
    insertInput.setSourceStart("191.200.0.0");
    insertInput.setSourceEnd("191.255.0.0");
    insertInput.setDestinationStart("191.200.0.0");
    insertInput.setDestinationEnd("191.255.0.0");

    mvcResult = testPost("", insertInput);

    checkInput = new CheckInput();
    checkInput.setSource("191.200.3.0");
    checkInput.setDestination("191.200.3.0");
    check = ipRuleController.check(checkInput);
    Assertions.assertEquals(check.getBody().getData(), false);
  }

  @Test
  void checkSubnetIpRule() throws Exception {

    InsertSubnetInput insertInput = new InsertSubnetInput();
    insertInput.setSpecificName("Test1");
    insertInput.setAllow(true);
    insertInput.setSubnetSource("165.72.83.194/19");
    insertInput.setSubnetDestination("165.72.83.194/19");

    MvcResult mvcResult = testPost("/subnet", insertInput);

    CheckInput checkInput = new CheckInput();
    checkInput.setSource("165.72.64.2");
    checkInput.setDestination("165.72.64.3");
    VgpResponse<Boolean> check = ipRuleController.check(checkInput);
    Assertions.assertEquals(check.getBody().getData(), true);

    Assertions.assertThrows(RuleNotFoundException.class, () -> {
      CheckInput checkInput2 = new CheckInput();
      checkInput2.setSource("192.100.3.0");
      checkInput2.setDestination("192.150.3.0");
      ipRuleController.check(checkInput2);
    });

    Assertions.assertThrows(BadRequestException.class, () -> {
      CheckInput checkInput2 = new CheckInput();
      checkInput2.setSource("300.100.3.0");
      checkInput2.setDestination("300.150.3.0");
      ipRuleController.check(checkInput2);
    });

    insertInput = new InsertSubnetInput();
    insertInput.setSpecificName("Test2");
    insertInput.setAllow(false);
    insertInput.setSubnetSource("165.72.83.194/19");
    insertInput.setSubnetDestination("165.72.83.194/19");
    mvcResult = testPost("/subnet", insertInput);

    checkInput = new CheckInput();
    checkInput.setSource("165.72.64.2");
    checkInput.setDestination("165.72.64.3");
    check = ipRuleController.check(checkInput);
    Assertions.assertEquals(check.getBody().getData(), false);

  }

  @Test
  void badRequestIPv4Addresses_Range() throws Exception {
    Assertions.assertThrows(BadRequestException.class, () -> {
      InsertInput insertInput = new InsertInput();
      insertInput.setSpecificName("Test1");
      insertInput.setAllow(true);
      insertInput.setSourceStart("300.200.0.0");
      insertInput.setSourceEnd("300.255.0.0");
      insertInput.setDestinationStart("192.200.0.0");
      insertInput.setDestinationEnd("192.255.0.0");
      ipRuleController.create(null,insertInput);
    });
  }

}
