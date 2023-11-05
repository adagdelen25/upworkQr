package com.upwork.hometask.demo.services.synchronize;

import com.upwork.hometask.demo.domain.IpRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class IpRuleSynchronizeMainStoreService {
    public void insertMainStore(IpRule ipRule) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
    }

    public void updateMainStore(IpRule ipRule) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
    }

    public void removeMainStore(IpRule ipRule) {
        //todo If you are going to use a distribution system, send the changes to the center from here.
    }
}
