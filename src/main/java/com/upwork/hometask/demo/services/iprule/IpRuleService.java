package com.upwork.hometask.demo.services.iprule;

import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.models.dto.IdOutput;
import com.upwork.hometask.demo.models.exception.AlreadyExistsException;
import com.upwork.hometask.demo.models.exception.BadRequestException;
import com.upwork.hometask.demo.repository.IpRuleRepository;
import com.upwork.hometask.demo.resources.iprule.model.InsertInput;
import com.upwork.hometask.demo.resources.iprule.model.SearchInput;
import com.upwork.hometask.demo.resources.iprule.model.SearchOutput;
import com.upwork.hometask.demo.resources.iprule.model.UpdateInput;
import com.upwork.hometask.demo.utils.IpUtil;
import com.upwork.hometask.demo.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class IpRuleService {

    private final IpRuleRepository ipRuleRepository;

    public SearchOutput search(SearchInput input) {
        Pageable pageable = PageUtils.getPageable(input);
        Page<IpRule> ipRules = null;
        if (StringUtils.isNotBlank(input.getName())) {
            ipRules = ipRuleRepository.findAllBySpecificNameNotContains(input.getName(), pageable);
        } else {
            ipRules = ipRuleRepository.findAll(pageable);
        }
        List<SearchOutput.SearchOutputData> searchOutputs = ipRules.stream().map(ipRule -> {
            SearchOutput.SearchOutputData searchOutput = new SearchOutput.SearchOutputData();
            searchOutput.setId(ipRule.getId());
            searchOutput.setSpecificName(ipRule.getSpecificName());
            searchOutput.setSourceStart(ipRule.getSourceStart());
            searchOutput.setSourceEnd(ipRule.getSourceEnd());
            searchOutput.setDestinationStart(ipRule.getDestinationStart());
            searchOutput.setDestinationEnd(ipRule.getDestinationEnd());
            searchOutput.setSubnetSource(ipRule.getSubnetSource());
            searchOutput.setSubnetDestination(ipRule.getSubnetDestination());
            searchOutput.setAllow(ipRule.getAllow());
            searchOutput.setIsSubnet(ipRule.getIsSubnet());
            return searchOutput;
        }).collect(Collectors.toList());
        return new SearchOutput(searchOutputs, ipRules.getNumber(), ipRules.getTotalPages(), ipRules.getTotalElements());
    }

    public IdOutput create(InsertInput input) {

        IpRule ipRule = new IpRule();
        ipRule.setSpecificName(input.getSpecificName());
        ipRule.setAllow(input.getAllow());

        ipRule.setSourceStart(input.getSourceStart());
        ipRule.setSourceEnd(input.getSourceEnd());
        ipRule.setSourceStartValue(IpUtil.ipToLong(input.getSourceStart()));
        ipRule.setSourceEndValue(IpUtil.ipToLong(input.getSourceEnd()));

        ipRule.setDestinationStart(input.getDestinationStart());
        ipRule.setDestinationEnd(input.getDestinationEnd());
        ipRule.setDestinationStartValue(IpUtil.ipToLong(input.getDestinationStart()));
        ipRule.setDestinationEndValue(IpUtil.ipToLong(input.getDestinationEnd()));

        ipRule.setCreatedAt(OffsetDateTime.now());
        ipRule.setCreatedBy("TestUser");

        validation(ipRule);

        ipRuleRepository.save(ipRule);
        log.info("An ip rule create SS: {} - SE:{}- DS:{} - DE:{}- A/D : {} ", ipRule.getSourceStart(), ipRule.getSourceEnd(), ipRule.getDestinationStart(), ipRule.getDestinationEnd(), ipRule.getAllow());
        return new IdOutput(ipRule.getId());
    }

    public void update(Long id, UpdateInput input) {
        IpRule ipRule = ipRuleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + id));
        ipRule.setSpecificName(input.getSpecificName());
        ipRule.setAllow(input.getAllow());

        ipRule.setSourceStart(input.getSourceStart());
        ipRule.setSourceEnd(input.getSourceEnd());
        ipRule.setSourceStartValue(IpUtil.ipToLong(input.getSourceStart()));
        ipRule.setSourceEndValue(IpUtil.ipToLong(input.getSourceEnd()));

        ipRule.setDestinationStart(input.getDestinationStart());
        ipRule.setDestinationEnd(input.getDestinationEnd());
        ipRule.setDestinationStartValue(IpUtil.ipToLong(input.getDestinationStart()));
        ipRule.setDestinationEndValue(IpUtil.ipToLong(input.getDestinationEnd()));
        ipRule.setModifiedAt(OffsetDateTime.now());
        ipRule.setModifiedBy("TestUser");

        ipRuleRepository.save(ipRule);
    }

    public void delete(Long id) {
        IpRule ipRule = ipRuleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not found given id: " + id));
        ipRuleRepository.delete(ipRule);

        log.info("An ip rule deleted SS: {} - SE:{}- DS:{} - DE:{} - SubS: {} - SubD : {} A/D : {} ", ipRule.getSourceStart(), ipRule.getSourceEnd(), ipRule.getDestinationStart(), ipRule.getDestinationEnd(), ipRule.getSubnetSource(), ipRule.getSubnetDestination(), ipRule.getAllow());

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
        List<IpRule> existingRules = ipRuleRepository.findAllBySourceStartValueAndSourceEndValueAndDestinationStartValueAndDestinationEndValueAndAllow(ipRule.getSourceStartValue(), ipRule.getSourceEndValue(), ipRule.getDestinationStartValue(), ipRule.getDestinationEndValue(), ipRule.getAllow());

        if (!existingRules.isEmpty()) {
            throw new AlreadyExistsException("A rule is defined exactly the same as this definition");
        }
    }

}
