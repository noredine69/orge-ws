package com.noredine69.orge.ws.business;

import com.noredine69.orge.ws.core.model.SearchRuleCriteria;
import com.noredine69.orge.ws.model.FeeDto;
import com.noredine69.orge.ws.model.FeeRequestDto;

public interface BusinessRateService {
    boolean checkRuleOnCriteria(final String criteria, final SearchRuleCriteria searchRuleCriteria, long id) throws IllegalAccessException, InstantiationException, ClassNotFoundException;
    FeeDto searchRate(FeeRequestDto feeRequestDto);
}
