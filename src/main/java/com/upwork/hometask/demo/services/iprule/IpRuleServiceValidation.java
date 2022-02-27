package com.upwork.hometask.demo.services.iprule;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.upwork.hometask.demo.domain.IpRule;
import com.upwork.hometask.demo.exception.BadRequestException;
import com.upwork.hometask.demo.models.dto.IdOutput;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class IpRuleServiceValidation {

  private final IpRuleRepository ipRuleRepository;

  public void validation(IpRule ipRule) {
    Optional<IpRule> ipRuleExists = ipRuleRepository.findBySpecificName(ipRule.getSpecificName());
    if (ipRuleExists.isPresent()) {
      throw new BadRequestException(ipRule.getSpecificName() + " is using. Please use another name");
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
      throw new BadRequestException("A rule is defined exactly the same as this definition");
    }
  }
}
