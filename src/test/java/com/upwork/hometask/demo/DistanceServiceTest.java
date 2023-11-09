package com.upwork.hometask.demo;

import com.upwork.hometask.demo.services.qrCode.DistanceService;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DistanceServiceTest implements WithAssertions {

    @InjectMocks
    private DistanceService distanceService;

    @BeforeEach
    public void setup() {

    }

    @Test
    void given_Coordinate_thenReturnTrue() {
        distanceService.setEnable(true);
        boolean b = distanceService.okDistance(1D, 1D, 1D, 1D);
        Assertions.assertEquals(b, true);
    }

    @Test
    void given_Coordinate_thenReturnFalse() {
        distanceService.setEnable(true);
        boolean b = distanceService.okDistance(1D, 1D, 2D, 2D);
        Assertions.assertEquals(b, false);
    }


}