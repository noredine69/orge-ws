package com.noredine69.orge.ws.core.service;

import com.noredine69.orge.ws.core.model.FeeRule;

import java.util.List;

public interface FeeRuleService {
    void insertFeeRule(FeeRule feeRule);
    FeeRule findFeeRuleById(Integer id);
    List<FeeRule> findAllFeeRule();
    List<FeeRule> findNotDefaultFeeRuleWithLocation(final String freelancerCountryIso2, final String clientCountryIso2);
    List<FeeRule> findNotDefaultFeeRule();
    FeeRule findDefaultFeeRule();
}
