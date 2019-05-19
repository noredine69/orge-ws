package com.noredine69.orge.ws.converter;

import com.google.common.base.Strings;
import com.noredine69.orge.ws.core.model.FeeRule;
import com.noredine69.orge.ws.model.RuleDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Slf4j
public class FeeRuleConverter {

    private static final String SEARCH_CRITERIA_INSTANCE_GETTER ="searchRuleCriteria.get";
    public static final String COUNTRY = "country";
    public static final String FREELANCER_LOCATION = "@freelancer.location";
    public static final String CLIENT_LOCATION = "@client.location";

    private FeeRuleConverter(){}

    public static FeeRule convertFeeRuleDtoToModel(final RuleDto body) {
        final Object restrictionObject = body.getRestrictions();
        FeeRule feeRule = new FeeRule();
        String sqlRestrictions = "";
        if (restrictionObject instanceof LinkedHashMap) {
            final LinkedHashMap restrictionsMap = (LinkedHashMap) restrictionObject;
            for (final Object keyObj : restrictionsMap.keySet()) {
                if (keyObj instanceof String) {
                    final String key = (String) keyObj;
                    final Object value = restrictionsMap.get(key);
                    log.debug("Object "+value.getClass());
                    if (value instanceof LinkedHashMap) {
                        final LinkedHashMap mapsCriteria = (LinkedHashMap) value;
                        log.debug("restrictions : " + key + "-" + mapsCriteria.keySet() + "-" + mapsCriteria.values());
                        //@freelancer.location-[country]-[ES]
                        //@client.location-[country]-[ES]
                        convertLocationCriteria(key.toString(), mapsCriteria);
                        readLocationCoutryCriteria(feeRule, key, mapsCriteria);
                    } else if (value instanceof ArrayList) {
                        final ArrayList listCriteria = (ArrayList) value;
                        log.debug("restrictions : " + key + "-" + listCriteria);
                        //@or-[{@mission.duration={gt=2months}}, {@commercialrelation.duration={gt=2months}}]
                        sqlRestrictions = treatOperandList(key.toString(), listCriteria);
                        log.debug("compute : " + sqlRestrictions );
                    }
                }
            }
            feeRule.setSqlRestrictions(sqlRestrictions);
            feeRule.setName(body.getName());
            feeRule.setRate(body.getRate().getPercent().longValue());
        }
        return feeRule;
    }

    private static void readLocationCoutryCriteria(FeeRule feeRule, String key, LinkedHashMap mapsCriteria) {
        final String country = (String) mapsCriteria.get(COUNTRY);
        if(StringUtils.isNotBlank(country)) {
            switch (key) {
                case FREELANCER_LOCATION:
                    feeRule.setFreelancerLocationCountry(country);
                    break;
                case CLIENT_LOCATION:
                    feeRule.setClientLocationCountry(country);
                    break;
            }
        }
    }

    private static String convertLocationCriteria(final String fieldName, final LinkedHashMap listOperand){
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

    private static String treatOperandList(final String operand, final ArrayList listCriteria) {
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
                concatenedCriteria +=  delimiterOperand + " "+ treatOperandList(operand, listOperand);
            }
            delimiterOperand = convertLogicialOperand(operand);
        }
        concatenedCriteria += " ) ";
        return concatenedCriteria;
    }

    private static String treatOperandList(String fieldName, final LinkedHashMap listOperand) {
        String filterdFieldName = filterFieldName(fieldName);
        String concatenedCriteria = " ( ";
        String delimiter = "";
        for(Object key : listOperand.keySet()) {
            Object value = listOperand.get(key);
            if(value != null) {
                concatenedCriteria += delimiter + filterdFieldName + convertOperand(key.toString()) + getNbDaysFromDuration(value.toString());
                delimiter = " and ";
            }
        }
        concatenedCriteria += " ) ";
        return concatenedCriteria;
    }

    private static long getNbDaysFromDuration(String durationRestriction) {
        Duration duration  = DurationConverter.parse(durationRestriction);
        return duration.toDays();
    }

    private static String filterFieldName(String fieldName) {
        if(!Strings.isNullOrEmpty(fieldName)){
            return SEARCH_CRITERIA_INSTANCE_GETTER +setCamelCase(fieldName.replaceAll("@", "").replaceAll("\\.", "_"))+"()";
        }
        return "";
    }

    private static String setCamelCase(String text) {
        if(!Strings.isNullOrEmpty(text)){
            return text.substring(0, 1).toUpperCase() +
                    text.substring(1).toLowerCase();
        }
        return "";
    }

    private static String convertLogicialOperand(String logicalOperand) {
        switch (logicalOperand) {
            case "@or":
                return " || ";
            case "@and" :
            default:
                return " && ";
        }
    }
    
    private static String convertOperand(String operand) {
        switch (operand) {
            case "gt":
                return " > ";
            case "lt":
                return " < ";
            case "gte":
                return " >= ";
            case "lte":
                return " <= ";
            case "neq":
                return " != ";
            case "eq":
            case "country" :
                return " == ";
            default:
                return "";
        }
    }
}
