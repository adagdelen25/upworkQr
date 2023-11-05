package com.upwork.hometask.demo;

import com.google.common.net.InetAddresses;
import com.upwork.hometask.demo.resources.iprule.model.InsertInput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles(value = "h2")
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.datasource.url=jdbc:h2:~/${random.uuid}"})
@AutoConfigureMockMvc
public class PerformanceTest {

    public static void main(String[] args) {

        for (int i = 0; i < 100; i++) {
            String sourceStart = InetAddresses.fromInteger(i*10).getHostAddress();
            String sourceEnd = InetAddresses.fromInteger(i*10+100).getHostAddress();

            String destinationStart = InetAddresses.fromInteger(i*10).getHostAddress();
            String destinationEnd = InetAddresses.fromInteger(i*10+100).getHostAddress();

            InsertInput insertInput = new InsertInput();
            insertInput.setSpecificName("Test"+i);
            insertInput.setAllow(true);
            insertInput.setSourceStart(sourceStart);
            insertInput.setSourceEnd(sourceEnd);
            insertInput.setDestinationStart(destinationStart);
            insertInput.setDestinationEnd(destinationEnd);



        }


    }

}
