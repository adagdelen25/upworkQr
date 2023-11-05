package com.upwork.hometask.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.InetAddresses;
import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.resources.iprule.IpRuleController;
import com.upwork.hometask.demo.resources.iprule.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles(value = "h2")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:~/${random.uuid}"})
@AutoConfigureMockMvc
class AppAllIntegrationTests {

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
        mvc.perform(get(basePath + path + params).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    protected <T> T testGet(String path, Object paramContent, Class<T> clazz) throws Exception {
        String s = testGet(path, paramContent);
        JsonNode getByIdNode = objectMapper.readTree(s);
        String userJsonStr = getByIdNode.toString();
        T output = objectMapper.readValue(userJsonStr, clazz);
        return output;
    }

    protected String testGet(String path, Object paramContent) throws Exception {
        if (paramContent == null) {
            MvcResult mvcResult = mvc.perform(get(basePath + path).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
            String contentResult = mvcResult.getResponse().getContentAsString();
            return contentResult;
        } else {
            MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(basePath + path);
            Map<String, Object> paramNames = objectMapper.convertValue(paramContent, Map.class);
            for (String key : paramNames.keySet()) {
                if (paramNames.get(key) != null)
                    mockHttpServletRequestBuilder.param(key, Objects.toString(paramNames.get(key)));
            }
            MvcResult mvcResult = mvc.perform(mockHttpServletRequestBuilder.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
            return mvcResult.getResponse().getContentAsString();
        }
    }

    protected MvcResult testPost(String path, Object content) throws Exception {
        if (content != null) {
            return mvc.perform(post(basePath + path).content(objectMapper.writeValueAsString(content)).contentType(MediaType.APPLICATION_JSON)).andReturn();
        } else {
            return mvc.perform(post(basePath + path).contentType(MediaType.APPLICATION_JSON)).andReturn();
        }
    }


    protected Long testPostGetId(String path, Object content) throws Exception {
        MvcResult mvcResult = null;
        if (content != null) {
            mvcResult = mvc.perform(post(basePath + path).content(objectMapper.writeValueAsString(content)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.CREATED.value())).andReturn();
        } else {
            mvcResult = mvc.perform(post(basePath + path).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.CREATED.value())).andReturn();
        }
        String contentResult = mvcResult.getResponse().getContentAsString();
        return getId(contentResult);
    }

    protected void testPut(String path, Object content) throws Exception {
        MvcResult mvcResult = null;
        if (content != null) {
            mvcResult = mvc.perform(put(basePath + path).content(objectMapper.writeValueAsString(content)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.NO_CONTENT.value())).andReturn();
        } else {
            mvcResult = mvc.perform(put(basePath + path).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is(HttpStatus.NO_CONTENT.value())).andReturn();
        }
    }

    public Long getId(String s) throws JsonProcessingException {
        try {
            JsonNode readTree = objectMapper.readTree(s);
            long id = readTree.get("id").asLong();
            return id;
        } catch (Exception e) {
            return -1L;
        }
    }

    protected void testDelete(String path, String params) throws Exception {
        mvc.perform(delete(basePath + path + params).content(params).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
    }


    @Test
    void addIPv4Addresses_Range() throws Exception {
        InsertInput insertInput = new InsertInput();
        insertInput.setSpecificName("Test1");
        insertInput.setAllow(true);
        insertInput.setSourceStart("500.200.0.0");
        insertInput.setSourceEnd("192.255.0.0");
        insertInput.setDestinationStart("192.200.0.0");
        insertInput.setDestinationEnd("192.255.0.0");

        // check bad request for ip pattern
        Assertions.assertThrows(AssertionError.class, () -> {
            testPostGetId("", insertInput);
        });

        // set valid ip
        insertInput.setSourceStart("192.200.0.0");

        // add IpRule
        Long ipRuleId = testPostGetId("", insertInput);

        IpRule ipRule = ipRuleRepository.findById(ipRuleId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + ipRuleId));

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


        // check bad request for uniquename
        Assertions.assertThrows(AssertionError.class, () -> {
            testPostGetId("", insertInput);
        });

        // check bad request for existing interval
        Assertions.assertThrows(AssertionError.class, () -> {
            insertInput.setSpecificName("Test2");
            testPostGetId("", insertInput);
        });


        // update IpRule

        UpdateInput updateInput = new UpdateInput();
        updateInput.setSpecificName("Test2");
        updateInput.setAllow(true);
        updateInput.setSourceStart("192.200.0.1");
        updateInput.setSourceEnd("192.255.0.1");
        updateInput.setDestinationStart("192.200.0.1");
        updateInput.setDestinationEnd("192.255.0.1");

        testPut("/" + ipRuleId, updateInput);

        ipRule = ipRuleRepository.findById(ipRuleId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + ipRuleId));

        Assertions.assertEquals(ipRule.getSpecificName(), "Test2");
        Assertions.assertEquals(ipRule.getAllow(), true);
        Assertions.assertEquals(ipRule.getSourceStart(), "192.200.0.1");
        Assertions.assertEquals(ipRule.getSourceEnd(), "192.255.0.1");
        Assertions.assertEquals(ipRule.getDestinationStart(), "192.200.0.1");
        Assertions.assertEquals(ipRule.getDestinationEnd(), "192.255.0.1");

    }


    @Test
    void addIPv4Addresses_Subnet_Range() throws Exception {
        InsertSubnetInput insertInput = new InsertSubnetInput();
        insertInput.setSpecificName("Test1");
        insertInput.setAllow(true);
        insertInput.setSubnetSource("165.72.83.194/19");
        insertInput.setSubnetDestination("165.72.83.194/19");

        Long ipRuleId = testPostGetId("/subnet", insertInput);


        IpRule ipRule = ipRuleRepository.findById(ipRuleId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + ipRuleId));

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

        UpdateSubnetInput updateInput = new UpdateSubnetInput();
        updateInput.setSpecificName("Test2");
        updateInput.setAllow(true);
        updateInput.setSubnetSource("165.72.83.194/20");
        updateInput.setSubnetDestination("165.72.83.194/21");

        testPut("/subnet/" + ipRuleId, updateInput);

        ipRule = ipRuleRepository.findById(ipRuleId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + ipRuleId));

        Assertions.assertEquals(ipRule.getSpecificName(), "Test2");
        Assertions.assertEquals(ipRule.getSubnetSource(), "165.72.83.194/20");
        Assertions.assertEquals(ipRule.getSubnetDestination(), "165.72.83.194/21");


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

        Long ipRuleId = testPostGetId("", insertInput);

        IpRule ipRule = ipRuleRepository.findById(ipRuleId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + ipRuleId));


        testDelete("/1", "");

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            ipRuleRepository.findById(ipRuleId).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + ipRuleId));
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

        testPost("", insertInput);

        CheckInput checkInput = new CheckInput();
        checkInput.setSource("192.200.3.0");
        checkInput.setDestination("192.200.3.0");


        Boolean check = testGet("/check", checkInput, Boolean.class);
        Assertions.assertEquals(check, true);

        Assertions.assertThrows(AssertionError.class, () -> {
            CheckInput checkInput2 = new CheckInput();
            checkInput2.setSource("192.100.3.0");
            checkInput2.setDestination("192.150.3.0");
            testGet("/check", checkInput2, Boolean.class);
        });

        insertInput = new InsertInput();
        insertInput.setSpecificName("Test2");
        insertInput.setAllow(false);
        insertInput.setSourceStart("191.200.0.0");
        insertInput.setSourceEnd("191.255.0.0");
        insertInput.setDestinationStart("191.200.0.0");
        insertInput.setDestinationEnd("191.255.0.0");

        testPost("", insertInput);

        checkInput = new CheckInput();
        checkInput.setSource("191.200.3.0");
        checkInput.setDestination("191.200.3.0");
        check = testGet("/check", checkInput, Boolean.class);
        Assertions.assertEquals(check, false);
        check = testGet("/check", checkInput, Boolean.class);
        Assertions.assertEquals(check, false);
    }

    @Test
    void checkSubnetIpRule() throws Exception {

        InsertSubnetInput insertInput = new InsertSubnetInput();
        insertInput.setSpecificName("Test1");
        insertInput.setAllow(true);
        insertInput.setSubnetSource("165.72.83.194/19");
        insertInput.setSubnetDestination("165.72.83.194/19");

        testPost("/subnet", insertInput);

        CheckInput checkInput = new CheckInput();
        checkInput.setSource("165.72.64.2");
        checkInput.setDestination("165.72.64.3");
        Boolean check = testGet("/check", checkInput, Boolean.class);
        Assertions.assertEquals(check, true);

        Assertions.assertThrows(AssertionError.class, () -> {
            CheckInput checkInput2 = new CheckInput();
            checkInput2.setSource("192.100.3.0");
            checkInput2.setDestination("192.150.3.0");
            testGet("/check", checkInput2, Boolean.class);
        });

        Assertions.assertThrows(AssertionError.class, () -> {
            CheckInput checkInput2 = new CheckInput();
            checkInput2.setSource("300.100.3.0");
            checkInput2.setDestination("300.150.3.0");
            testGet("/check", checkInput2, Boolean.class);
        });

        insertInput = new InsertSubnetInput();
        insertInput.setSpecificName("Test2");
        insertInput.setAllow(false);
        insertInput.setSubnetSource("165.72.83.194/19");
        insertInput.setSubnetDestination("165.72.83.194/19");
        testPost("/subnet", insertInput);

        checkInput = new CheckInput();
        checkInput.setSource("165.72.64.2");
        checkInput.setDestination("165.72.64.3");
        check = testGet("/check", checkInput, Boolean.class);
        Assertions.assertEquals(check, false);

    }

    /*
    open if you want to performance test because many thread created
     */

//    @Test
    void performanceTest() throws Exception {
        for (int i = 0; i < 100; i++) {
            String sourceStart = InetAddresses.fromInteger(i * 10).getHostAddress();
            String sourceEnd = InetAddresses.fromInteger(i * 10 + 100).getHostAddress();

            String destinationStart = InetAddresses.fromInteger(i * 10).getHostAddress();
            String destinationEnd = InetAddresses.fromInteger(i * 10 + 100).getHostAddress();

            InsertInput insertInput = new InsertInput();
            insertInput.setSpecificName("AllowTest" + i);
            insertInput.setAllow(true);
            insertInput.setSourceStart(sourceStart);
            insertInput.setSourceEnd(sourceEnd);
            insertInput.setDestinationStart(destinationStart);
            insertInput.setDestinationEnd(destinationEnd);
            testPostGetId("", insertInput);
        }

        for (int i = 0; i < 20; i++) {
            String sourceStart = InetAddresses.fromInteger(i * 10).getHostAddress();
            String sourceEnd = InetAddresses.fromInteger(i * 10 + 100).getHostAddress();

            String destinationStart = InetAddresses.fromInteger(i * 10).getHostAddress();
            String destinationEnd = InetAddresses.fromInteger(i * 10 + 100).getHostAddress();

            InsertInput insertInput = new InsertInput();
            insertInput.setSpecificName("DenyTest" + i);
            insertInput.setAllow(false);
            insertInput.setSourceStart(sourceStart);
            insertInput.setSourceEnd(sourceEnd);
            insertInput.setDestinationStart(destinationStart);
            insertInput.setDestinationEnd(destinationEnd);
            testPostGetId("", insertInput);
        }

        int numberOfThreads = 1000;
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                String threadCode = RandomStringUtils.randomAlphanumeric(5);
                try {
                    int j = RandomUtils.nextInt(0, 120);
                    int nextSource = RandomUtils.nextInt(j * 10, j * 10 + 100);
                    int nextDestination = RandomUtils.nextInt(j * 10, j * 10 + 100);
                    String source = InetAddresses.fromInteger(nextSource).getHostAddress();
                    String destination = InetAddresses.fromInteger(nextDestination).getHostAddress();
                    CheckInput checkInput = new CheckInput();
                    checkInput.setSource(source);
                    checkInput.setDestination(destination);
                    Boolean check = testGet("/check", checkInput, Boolean.class);
                    System.out.println(threadCode + " thread :: " + check);
                } catch (Exception e) {
                    // Handle exception
                }
                latch.countDown();
            });
        }
        latch.await();
    }
}

