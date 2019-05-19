package com.noredine69.orge.ws.business.impl;


import com.noredine69.orge.ws.business.BusinessFeeRuleService;
import com.noredine69.orge.ws.converter.FeeRuleConverter;
import com.noredine69.orge.ws.core.model.FeeRule;
import com.noredine69.orge.ws.core.service.FeeRuleService;
import com.noredine69.orge.ws.model.RuleDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BusinessFeeRuleServiceImpl implements BusinessFeeRuleService{

    @Autowired
    private FeeRuleService feeRuleService;

    @Override
    public void addNewRule(final RuleDto body) {
        FeeRule feeRule = FeeRuleConverter.convertFeeRuleDtoToModel(body);
        log.debug("FeeRule to insert: " + feeRule );
        feeRuleService.insertFeeRule(feeRule);
        log.debug("findAllFeeRule : " + feeRuleService.findAllFeeRule() );
    }
}
