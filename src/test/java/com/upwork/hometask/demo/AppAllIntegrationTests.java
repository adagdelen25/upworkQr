package com.upwork.hometask.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upwork.hometask.demo.resources.qrcode.model.CheckInInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityInput;
import com.upwork.hometask.demo.resources.qrcode.model.CurrentActivityOutput;
import com.upwork.hometask.demo.resources.qrcode.model.ScheduleOutput;
import com.upwork.hometask.demo.services.qrCode.EncryptService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;
import java.util.Objects;

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
    protected EncryptService encryptService;

    @Autowired
    protected ObjectMapper objectMapper;

    private final static String basePath = "/api/qrCode";

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
            return mvc.perform(post(basePath + path).content(objectMapper.writeValueAsString(content)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        } else {
            return mvc.perform(post(basePath + path).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andReturn();
        }
    }

    protected void testPut(String path, Object content) throws Exception {
        MvcResult mvcResult = null;
        if (content != null) {
            mvcResult = mvc.perform(put(basePath + path).content(objectMapper.writeValueAsString(content)).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
        } else {
            mvcResult = mvc.perform(put(basePath + path).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent()).andReturn();
        }
    }

    @Test
    void currentActivityService() throws Exception {

        testPost("/dummy/createData", null);

        ScheduleOutput scheduleOutput = testGet("/dummy/listSchedule", null, ScheduleOutput.class);

        CurrentActivityInput input = new CurrentActivityInput();
        Long studentId = scheduleOutput.getStudents().get(0).getStudentId();
        Long nextStudentId = scheduleOutput.getStudents().get(1).getStudentId();
        input.setStudentId(studentId);
        String qrCode = scheduleOutput.getClassrooms().get(0).getQrCode();
        String qrCodeNext = scheduleOutput.getClassrooms().get(1).getQrCode();

        // error if qrcode is null
        AssertionError error1 = Assertions.assertThrows(AssertionError.class, () -> {
            CurrentActivityOutput output = testGet("/listCurrentActivity", input, CurrentActivityOutput.class);
        });

        input.setQrCode(qrCode);
        CurrentActivityOutput output = testGet("/listCurrentActivity", input, CurrentActivityOutput.class);
        if (!output.getActivities().isEmpty()) {
            final CurrentActivityOutput.CurrentActivity currentActivity = output.getActivities().get(0);
            final Long scheduleId = currentActivity.getScheduleId();
            final CheckInInput checkInInput = new CheckInInput();
            checkInInput.setCorrelationID(output.getCorrelationID());
            checkInInput.setStudentId(nextStudentId);
            checkInInput.setScheduleId(scheduleId);
            // fail if read qr code belongs another person
            Assertions.assertThrows(AssertionError.class, () -> {
                testPut("/checkIn", checkInInput);
            });

            // success if qrcode and time is ok
            checkInInput.setStudentId(studentId);
            testPut("/checkIn", checkInInput);

            // fail if two times checkin same qr code
            Assertions.assertThrows(AssertionError.class, () -> {
                testPut("/checkIn", checkInInput);
            });

            // fail if checkin already checkin another class at the same time
            checkInInput.setScheduleId(output.getActivities().get(1).getScheduleId());
                testPut("/checkIn", checkInInput);
        }
    }

    @Test
    void currentActivityServiceExpireTime() throws Exception {
        testPost("/dummy/createData", null);
        ScheduleOutput scheduleOutput = testGet("/dummy/listSchedule", null, ScheduleOutput.class);
        CurrentActivityInput input = new CurrentActivityInput();
        Long studentId = scheduleOutput.getStudents().get(0).getStudentId();
        input.setStudentId(studentId);
        String qrCode = scheduleOutput.getClassrooms().get(0).getQrCode();
        input.setQrCode(qrCode);
        CurrentActivityOutput output = testGet("/listCurrentActivity", input, CurrentActivityOutput.class);
        if (!output.getActivities().isEmpty()) {
            Thread.sleep(15000);
            final CurrentActivityOutput.CurrentActivity currentActivity = output.getActivities().get(0);
            final Long scheduleId = currentActivity.getScheduleId();
            final CheckInInput checkInInput = new CheckInInput();
            checkInInput.setCorrelationID(output.getCorrelationID());
            checkInInput.setScheduleId(scheduleId);
            // fail if time is expired
            Assertions.assertThrows(AssertionError.class, () -> {
                testPut("/checkIn", checkInInput);
            });
        }
    }

}

