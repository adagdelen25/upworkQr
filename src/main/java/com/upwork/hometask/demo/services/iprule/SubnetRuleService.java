package com.upwork.hometask.demo.services.iprule;

import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.dto.IdOutput;
import com.upwork.hometask.demo.models.exception.AlreadyExistsException;
import com.upwork.hometask.demo.models.exception.BadRequestException;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.resources.iprule.model.InsertSubnetInput;
import com.upwork.hometask.demo.resources.iprule.model.UpdateSubnetInput;
import com.upwork.hometask.demo.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubnetRuleService {

    private final IpRuleRepository ipRuleRepository;
    private final IpRuleCheckService ipRuleCheckService;

    public IdOutput create(InsertSubnetInput input) {
        IpRule ipRule = new IpRule();
        ipRule.setSpecificName(input.getSpecificName());
        ipRule.setAllow(input.getAllow());
        ipRule.setIsSubnet(true);

        String[] sourceIps = getIps(input.getSubnetSource());
        ipRule.setSubnetSource(input.getSubnetSource());
        ipRule.setSourceStart(sourceIps[0]);
        ipRule.setSourceEnd(sourceIps[1]);
        ipRule.setSourceStartValue(IpUtil.ipToLong(sourceIps[0]));
        ipRule.setSourceEndValue(IpUtil.ipToLong(sourceIps[1]));

        String[] destinationIps = getIps(input.getSubnetDestination());
        ipRule.setSubnetDestination(input.getSubnetDestination());
        ipRule.setDestinationStart(destinationIps[0]);
        ipRule.setDestinationEnd(destinationIps[1]);
        ipRule.setDestinationStartValue(IpUtil.ipToLong(destinationIps[0]));
        ipRule.setDestinationEndValue(IpUtil.ipToLong(destinationIps[1]));

        ipRule.setCreatedAt(OffsetDateTime.now());
        ipRule.setCreatedBy("TestUser");

        validation(ipRule);

        ipRuleRepository.save(ipRule);

        log.info("An subnet rule created SS: {} - SD:{} - A/D : {} ",
                ipRule.getSubnetSource(),
                ipRule.getSubnetDestination(),
                ipRule.getAllow()
        );
        return new IdOutput(ipRule.getId());
    }

    public void update(Long id, UpdateSubnetInput input) {
        IpRule ipRule =
                ipRuleRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + id));
        ipRule.setSpecificName(input.getSpecificName());
        ipRule.setAllow(input.getAllow());
        ipRule.setIsSubnet(true);
        ipRule.setSubnetDestination(input.getSubnetDestination());
        ipRule.setSubnetSource(input.getSubnetSource());

        String[] sourceIps = getIps(input.getSubnetSource());
        ipRule.setSourceStartValue(IpUtil.ipToLong(sourceIps[0]));
        ipRule.setSourceEndValue(IpUtil.ipToLong(sourceIps[1]));

        String[] destinationIps = getIps(input.getSubnetDestination());
        ipRule.setDestinationStartValue(IpUtil.ipToLong(destinationIps[0]));
        ipRule.setDestinationEndValue(IpUtil.ipToLong(destinationIps[1]));
        ipRule.setModifiedAt(OffsetDateTime.now());
        ipRule.setModifiedBy("TestUser");
        ipRuleRepository.save(ipRule);

    }


    private static String[] getIps(String source) {

        String[] ipAndMask = source.split("\\/");
        String ip = ipAndMask[0];
        String mask = ipAndMask[1];

        String ipToBinary = "";
        String[] split2 = ip.split("\\.");
        for (String s : split2) {
            ipToBinary = ipToBinary + decToBinary(Integer.parseInt(s));
        }
        String withoutMask = ipToBinary.substring(0, Integer.parseInt(mask));
        String network = StringUtils.rightPad(withoutMask, 32, "0");
        String broadcast = StringUtils.rightPad(withoutMask, 32, "1");

        String[] networkArray = network.split("(?<=\\G.{" + 8 + "})");
        String[] broadcastArray = broadcast.split("(?<=\\G.{" + 8 + "})");

        String firstIp = "";
        for (int i = 0; i < networkArray.length; i++) {
            if (i == 0) {
                firstIp = "" + Integer.parseInt(networkArray[i], 2);
            } else if (i == 3) {
                firstIp = firstIp + "." + (Integer.parseInt(networkArray[i], 2) + 1);
            } else {
                firstIp = firstIp + "." + (Integer.parseInt(networkArray[i], 2));
            }
        }
        String secondIp = "";
        for (int i = 0; i < broadcastArray.length; i++) {
            if (i == 0) {
                secondIp = "" + Integer.parseInt(broadcastArray[i], 2);
            } else if (i == 3) {
                secondIp = secondIp + "." + (Integer.parseInt(broadcastArray[i], 2) - 1);
            } else {
                secondIp = secondIp + "." + (Integer.parseInt(broadcastArray[i], 2));
            }
        }
        return new String[]{firstIp, secondIp};
    }

    static String decToBinary(int n) {
        int[] binaryNum = {0, 0, 0, 0, 0, 0, 0, 0};
        int i = 0;
        while (n > 0) {
            binaryNum[i] = n % 2;
            n = n / 2;
            i++;
        }
        String a = "";
        for (int j = 7; j >= 0; j--) {
            a += binaryNum[j];
        }
        return a;
    }

    public void validation(IpRule ipRule) {
        Optional<IpRule> ipRuleExists = ipRuleRepository.findBySpecificName(ipRule.getSpecificName());
        if (ipRuleExists.isPresent()) {
            throw new AlreadyExistsException(ipRule.getSpecificName() + " is using. Please use another name");
        }

        if (ipRule.getDestinationEndValue() < ipRule.getDestinationStartValue()) {
            throw new BadRequestException("Destination start Ip must be lower than Destination End Ip");
        }
        if (ipRule.getSourceEndValue() < ipRule.getSourceStartValue()) {
            throw new BadRequestException("Source start Ip must be lower than Source End Ip");
        }
        List<IpRule> existingRules =
                ipRuleRepository
                        .findAllBySourceStartValueAndSourceEndValueAndDestinationStartValueAndDestinationEndValueAndAllow(
                                ipRule.getSourceStartValue(),
                                ipRule.getSourceEndValue(),
                                ipRule.getDestinationStartValue(),
                                ipRule.getDestinationEndValue(),
                                ipRule.getAllow()
                        );

        if (!existingRules.isEmpty()) {
            throw new AlreadyExistsException("A rule is defined exactly the same as this definition");
        }
    }
}
