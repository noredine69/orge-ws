package com.noredine69.orge.ws.core.service;

import com.noredine69.orge.ws.core.model.FeeRule;

import java.util.List;

public interface FeeRuleService {
    void insertFeeRule(FeeRule feeRule);
    FeeRule findFeeRuleById(Integer id);
    List<FeeRule> findAllFeeRule();
}
