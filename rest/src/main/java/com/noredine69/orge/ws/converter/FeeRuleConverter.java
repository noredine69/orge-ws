package com.noredine69.orge.ws.converter;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.noredine69.orge.ws.core.model.FeeRule;
import com.noredine69.orge.ws.model.RuleDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Slf4j
public class FeeRuleConverter {

    private static final String SEARCH_CRITERIA_INSTANCE_GETTER ="searchRuleCriteria.get";
    public static final String COUNTRY = "country";
    public static final String FREELANCER_LOCATION = "@freelancer.location";
    public static final String CLIENT_LOCATION = "@client.location";

    private FeeRuleConverter(){}

    public static FeeRule convertBodyToFeeRule(final String body) {

        final JsonParser parser = new JsonParser();
        final JsonElement jsonTree = parser.parse(body);
        String feeRuleName = "";
        long feeRuleRate = 0L;
        String feeRuleSqlRestrictions = "";
        String clientCountry  = "";
        String freelancerCountry = "";

        if (jsonTree.isJsonObject()) {
            final JsonObject jsonRootObject = jsonTree.getAsJsonObject();
            final JsonElement jsonNameElement = jsonRootObject.get("name");
            log.debug("name " + jsonNameElement.getAsString());
            feeRuleName = jsonNameElement.getAsString();
            final JsonElement rate = jsonRootObject.get("rate");
            if (rate.isJsonObject()) {
                final JsonObject jsonRateObject = rate.getAsJsonObject();
                final JsonElement percent = jsonRateObject.get("percent");

                log.debug("rate percent " + percent.getAsString());
                feeRuleRate = percent.getAsLong();
                final JsonObject jsonRestrictionsObject = jsonRootObject.get("restrictions").getAsJsonObject();
                clientCountry  = extractLocationFromJsonNode(jsonRestrictionsObject.get("@client.location"));
                freelancerCountry = extractLocationFromJsonNode(jsonRestrictionsObject.get("@freelancer.location"));

                jsonRestrictionsObject.get("@freelancer.location");
                String operand = "@or";
                JsonElement jsonOperandElement = jsonRestrictionsObject.get(operand);
                if (jsonOperandElement != null) {
                    feeRuleSqlRestrictions = streamJsonNode(jsonOperandElement, convertLogicialOperand(operand));
                } else {
                    operand = "@and";
                    jsonOperandElement = jsonRestrictionsObject.get(operand);
                    if (jsonOperandElement != null) {
                        feeRuleSqlRestrictions = streamJsonNode(jsonOperandElement, convertLogicialOperand(operand));
                    }
                }

            }
            log.debug("sqlRestrictions " + feeRuleSqlRestrictions);
        }
        return FeeRule.builder()
                .name(feeRuleName)
                .rate(feeRuleRate)
                .freelancerLocationCountry(freelancerCountry)
                .clientLocationCountry(clientCountry)
                .sqlRestrictions(feeRuleSqlRestrictions)
                .build();
    }

    private static String extractLocationFromJsonNode(JsonElement jsonElement) {
        if(jsonElement != null && jsonElement.isJsonObject()) {
            JsonElement jsonCountryElement = jsonElement.getAsJsonObject().get(COUNTRY);
            if(jsonCountryElement != null && jsonCountryElement.isJsonPrimitive()) {
                return jsonCountryElement.getAsString();
            }
        }
        return "";
    }


    private static String streamJsonNode(final JsonElement jsonElement, final String operand) {
        log.debug("operand : {}", operand);
        final StringBuilder valueJsonNode = new StringBuilder();
        if (jsonElement != null && jsonElement.isJsonArray()) {
            log.debug("jsonElement.isJsonArray()");
            final List<String> list = new ArrayList<>();
            final JsonArray jsonArray = jsonElement.getAsJsonArray();
            jsonArray.getAsJsonArray().forEach((jsonArrayElement) -> {
                list.add(" (" + streamJsonNode(jsonArrayElement, operand) + ") ");
            });
            final String finalRequest = String.join(operand, list);
            log.debug("         {}", finalRequest);
            valueJsonNode.append(finalRequest);
        }
        if (jsonElement != null && jsonElement.isJsonObject()) {
            log.debug("jsonElement.isJsonObject()");
            final JsonObject jsonObject = jsonElement.getAsJsonObject();
            jsonObject.entrySet().stream().forEach((propertyName) -> {
                log.debug("  {} {}", propertyName.getKey(), propertyName.getValue());
                String subOperand = convertLogicialOperand(propertyName.getKey());
                if (StringUtils.isNotBlank(subOperand)) {
                    String value = streamJsonNode(propertyName.getValue(), subOperand);
                    log.debug("#### value {}", value);
                    valueJsonNode.append(value);
                }else{
                    final String filteredFieldName = filterFieldName(propertyName.getKey());
                    log.debug("  {} is a final value {}", filteredFieldName, propertyName.getValue());
                    final JsonElement jsonFinalElement = propertyName.getValue();
                    if(jsonFinalElement.isJsonObject()) {
                        final JsonObject jsonFinalObject = jsonFinalElement.getAsJsonObject();
                        final List<String> list = new ArrayList<>();
                        jsonFinalObject.entrySet().stream().forEach((propertyFinalName) -> {
                            final String operator = convertOperator(propertyFinalName.getKey());
                            final long value = getNbDaysFromDuration(propertyFinalName.getValue().getAsString());
                            //log.debug("         {} {} {}", filteredFieldName, operator, value);
                            list.add(" " + filteredFieldName + operator + value+" ");
                        });
                        final String finalRequest = String.join(" and ", list);
                        log.debug("         {}", finalRequest);
                        valueJsonNode.append(finalRequest);
                    }
                }
            });
        }
        return valueJsonNode.toString();
    }

    public static long getNbDaysFromDuration(String durationRestriction) {
        try {
            Duration duration = DurationConverter.parse(durationRestriction);
            return duration.toDays();
        }catch(IllegalArgumentException |NullPointerException e) {
            log.warn("Impossible to parse {} to Duration {}", durationRestriction, e);
            return 0L;
        }
    }

    public static String filterFieldName(final String fieldName) {
        if(!Strings.isNullOrEmpty(fieldName)){
            return SEARCH_CRITERIA_INSTANCE_GETTER +setCamelCase(fieldName.replaceAll("@", "").replaceAll("\\.", "_"))+"()";
        }
        return "";
    }

    public static String setCamelCase(final String text) {
        if(StringUtils.isNotEmpty(text)){
            return text.substring(0, 1).toUpperCase() +
                    text.substring(1).toLowerCase();
        }
        return "";
    }

    public static String convertLogicialOperand(String logicalOperand) {
        if(StringUtils.isNotEmpty(logicalOperand)) {
            switch (logicalOperand) {
                case "@or":
                    return "||";
                case "@and":
                    return "&&";
                default:
                    return "";
            }
        }
        return "";
    }

    public static String convertOperator(String operator) {
        if(StringUtils.isNotEmpty(operator)) {
            switch (operator) {
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
                    return " == ";
                default:
                    return "";
            }
        }
        return "";
    }
}
