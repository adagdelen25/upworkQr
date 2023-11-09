package com.upwork.hometask.demo.services.qrCode;

import com.upwork.hometask.demo.utils.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistanceService {
    private static final double MAX_DISTANCE_IN_METER = 10D;

    public boolean enabled() {
        return false;
    }

    public boolean okDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (!enabled()) {
            return true;
        }
        double m = MapUtils.distance(lat1, lon1, lat2, lon2, "M");
        if (m > MAX_DISTANCE_IN_METER) {
            return false;
        }
        return true;
    }

}
