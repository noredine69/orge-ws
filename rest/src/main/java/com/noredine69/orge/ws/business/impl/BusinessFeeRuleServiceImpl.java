package com.noredine69.orge.ws.business.impl;


import com.google.gson.Gson;
import com.noredine69.orge.ws.business.BusinessFeeRuleService;
import com.noredine69.orge.ws.converter.FeeRuleConverter;
import com.noredine69.orge.ws.core.model.Country;
import com.noredine69.orge.ws.core.model.FeeRule;
import com.noredine69.orge.ws.core.service.CountryService;
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

    @Autowired
    private CountryService countryService;

    @Override
    public void addNewRule(final RuleDto body) {
        final String serializedRuleDto = serializeRuleDto(body);
        final FeeRule feeRule = FeeRuleConverter.convertBodyToFeeRule(serializedRuleDto);
        feeRule.setClientLocationCountryId(readCountryCodeFromIso2(feeRule.getClientLocationCountry()));
        feeRule.setFreelancerLocationCountryId(readCountryCodeFromIso2(feeRule.getFreelancerLocationCountry()));

        log.debug("FeeRule to insert: " + feeRule );
        feeRuleService.insertFeeRule(feeRule);
        log.debug("findAllFeeRule : " + feeRuleService.findAllFeeRule() );
    }

    private long readCountryCodeFromIso2(final String iso2){
        final Country clientCountry = countryService.findCountryIso2(iso2);
        if(clientCountry != null) {
            return clientCountry.getCode();
        }
        return 0;
    }
    //It's ugly to deserialize json Data, then serialize, to finally deserialize it!
    //sorry....
    private String serializeRuleDto(final RuleDto body) {
        Gson gson = new Gson();
        return gson.toJson(body);
    }
}
