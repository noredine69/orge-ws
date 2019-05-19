package com.noredine69.orge.ws.core.service.impl;

import com.noredine69.orge.ws.core.mapper.FeeRuleMapper;
import com.noredine69.orge.ws.core.model.FeeRule;
import com.noredine69.orge.ws.core.service.FeeRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeeRuleServiceImpl implements FeeRuleService {

    private final FeeRuleMapper feeRuleMapper;

    @Autowired
    public FeeRuleServiceImpl(final FeeRuleMapper mapper) {
        this.feeRuleMapper = mapper;
    }

    public void insertFeeRule(FeeRule feeRule) {
        feeRuleMapper.insertFeeRule(feeRule);
    }

    public FeeRule findFeeRuleById(Integer id) {
        return feeRuleMapper.findFeeRuleById(id);
    }

    public List<FeeRule> findAllFeeRule() {
        return feeRuleMapper.findAllFeeRule();
    }

    public List<FeeRule> findNotDefaultFeeRuleWithLocation(String freelancerCountryIso2, String clientCountryIso2) {
        return feeRuleMapper.findNotDefaultFeeRuleWithLocation(freelancerCountryIso2, clientCountryIso2);
    }

    public List<FeeRule> findNotDefaultFeeRule() {
        return feeRuleMapper.findNotDefaultFeeRule();
    }

    public FeeRule findDefaultFeeRule() {
        return feeRuleMapper.findDefaultFeeRule();
    }
}
