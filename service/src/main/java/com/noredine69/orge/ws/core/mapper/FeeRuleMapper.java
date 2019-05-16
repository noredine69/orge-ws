package com.noredine69.orge.ws.core.mapper;

import com.noredine69.orge.ws.core.model.FeeRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FeeRuleMapper {
    void insertFeeRule(@Param("feeRule") FeeRule feeRule);
    FeeRule findFeeRuleById(@Param("id") Integer id);
    List<FeeRule> findAllFeeRule();
}
