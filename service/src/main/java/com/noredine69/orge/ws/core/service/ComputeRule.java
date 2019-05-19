package com.noredine69.orge.ws.core.service;


import com.noredine69.orge.ws.core.model.SearchRuleCriteria;

public interface ComputeRule {
    boolean checkRule(final SearchRuleCriteria searchRuleCriteria);
}
