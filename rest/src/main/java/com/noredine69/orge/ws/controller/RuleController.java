package com.noredine69.orge.ws.controller;

import com.google.common.base.Strings;
import com.noredine69.orge.ws.api.RuleApi;
import com.noredine69.orge.ws.core.model.FeeRule;
import com.noredine69.orge.ws.core.service.FeeRuleService;
import com.noredine69.orge.ws.model.RuleDto;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Slf4j
@RestController
public class RuleController implements RuleApi {

    @Autowired
    private FeeRuleService feeRuleService;

    public ResponseEntity<Void> addRule(@ApiParam(value = "", required = true) @Valid @RequestBody final RuleDto body) {
        final Object restrictionObject = body.getRestrictions();
        FeeRule feeRule = new FeeRule();
        String sqlRestrictions = "";
        if (restrictionObject instanceof LinkedHashMap) {
            final LinkedHashMap restrictionsMap = (LinkedHashMap) restrictionObject;
            for (final Object keyObj : restrictionsMap.keySet()) {
                if (keyObj instanceof String) {
                    final String key = (String) keyObj;
                    final Object value = restrictionsMap.get(key);
                    System.out.println("Object "+value.getClass());
                    if (value instanceof LinkedHashMap) {
                        final LinkedHashMap maps = (LinkedHashMap) value;
                        //log.debug("restrictions : " + key + "-" + maps.keySet() + "-" + maps.values());
                        //@freelancer.location-[country]-[ES]
                        //@client.location-[country]-[ES]
                        System.out.println("restrictions : " + key + "-" + maps.keySet() + "-" + maps.values());
                    } else if (value instanceof ArrayList) {
                        final ArrayList listCriteria = (ArrayList) value;
                        //log.debug("restrictions : " + key + "-" + list.get(0) + "-" + list.get(1));
                        //@or-[{@mission.duration={gt=2months}}, {@commercialrelation.duration={gt=2months}}]
                        System.out.println("restrictions : " + key + "-" + listCriteria);
                        sqlRestrictions = treatOperandList(key.toString(), listCriteria);
                        System.out.println("compute : " + sqlRestrictions );

                    }
                }
            }
            feeRule.setId(11);
            feeRule.setSqlRestrictions(sqlRestrictions);
            feeRule.setName(body.getName());
            feeRule.setRate(body.getRate().getPercent().longValue());
            feeRuleService.insertFeeRule(feeRule);
            System.out.println("findAllFeeRule : " + feeRuleService.findAllFeeRule() );
        }
        return null;
    }

    private String treatOperandList(String operand, final ArrayList listCriteria) {
        String delimiterOperand = "";
        String concatenedCriteria = " ( ";
        for(Object current : listCriteria){

            if (current instanceof LinkedHashMap) {
                final LinkedHashMap maps = (LinkedHashMap) current;
                for(Object key : maps.keySet()) {
                    Object value = maps.get(key);
                    if(value instanceof LinkedHashMap) {
                        concatenedCriteria += delimiterOperand + " " + treatOperandList(key.toString(), (LinkedHashMap) value);
                    }else if(value instanceof ArrayList) {
                        concatenedCriteria += delimiterOperand + " "+ treatOperandList(key.toString(), (ArrayList)value);
                    }
                }
            } else if (current instanceof ArrayList) {
                final ArrayList listOperand = (ArrayList) current;
                concatenedCriteria += delimiterOperand + " "+ treatOperandList(operand, listOperand);
            }
            delimiterOperand = operand;
        }
        concatenedCriteria += " ) ";
        return concatenedCriteria;
    }

    private String treatOperandList(String fieldName, final LinkedHashMap listOperand) {
        String filterdFieldName = filterFieldName(fieldName);
        String concatenedCriteria = " ( ";
        String delimiter = "";
        for(Object key : listOperand.keySet()) {
            concatenedCriteria += delimiter + filterdFieldName + convertOperand(key.toString()) + listOperand.get(key);
            delimiter = " and ";
        }
        concatenedCriteria += " ) ";
        return concatenedCriteria;
    }

    private String filterFieldName(String fieldName) {
        if(!Strings.isNullOrEmpty(fieldName)){
            return fieldName.replaceAll("@", "").replaceAll("\\.", "_");
        }
        return "";
    }

    private String convertOperand(String operand) {
        switch (operand) {
            case "gt":
                return " > ";
            case "lt":
                return " > ";
            case "gte":
                return " >= ";
            case "lte":
                return " <= ";
            case "eq":
            case "country" :
                return " = ";
            default:
                return "";
        }
    }
}
