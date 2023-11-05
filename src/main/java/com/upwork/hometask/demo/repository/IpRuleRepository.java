package com.upwork.hometask.demo.repository;

import java.util.List;
import java.util.Optional;
import com.upwork.hometask.demo.domain.IpRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IpRuleRepository extends JpaRepository<IpRule, Long>, JpaSpecificationExecutor<IpRule> {

  Optional<IpRule> findBySpecificName(String specificName);

  Page<IpRule> findAllBySpecificNameNotContains(String name,Pageable pageable);
  @Query(
      "select c from IpRule as c  " +
          "where " +
          "(:ipFrom between c.sourceStartValue and c.sourceEndValue) " +
          "and (:ipTo between c.destinationStartValue and c.destinationEndValue)")
  List<IpRule> listAvailableIpRule(@Param("ipFrom") Long ipFrom,@Param("ipTo") Long ipTo);
  List<IpRule> findAllBySourceStartValueAndSourceEndValueAndDestinationStartValueAndDestinationEndValueAndAllow(Long sourceStartValue,Long sourceEndValue,Long destinationStartValue,Long destinationEndValue,Boolean allow );
}