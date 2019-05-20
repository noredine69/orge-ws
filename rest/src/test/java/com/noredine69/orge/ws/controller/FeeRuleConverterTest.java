package com.noredine69.orge.ws.controller;

import com.noredine69.orge.ws.core.model.FeeRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static com.noredine69.orge.ws.converter.FeeRuleConverter.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class FeeRuleConverterTest {
    private static final String SEARCH_CRITERIA_INSTANCE_GETTER ="searchRuleCriteria.get";
    private static final String jsonSimple_sqlRestrictions = " ( searchRuleCriteria.getMission_duration() > 60 ) || ( searchRuleCriteria.getCommercialrelation_duration() > 60 ) ";
    //@formatter:off
    private static final String jsonSimple =
            "{\n" +
                "\"name\":\"spain or repeat\", " +
                "\"rate\":{\"percent\":8 }, " +
                "\"restrictions\":{\n" +
                    "\"@or\":[\n" +
                        "{\n" +
                            "\"@mission.duration\":" +
                                "{\"gt\":\"2months\"} " +
                        "}, " +
                        "{\n" +
                            "\"@commercialrelation.duration\":" +
                                "{\"gt\":\"2months\" } " +
                        "} " +
                    "], " +
                    "\"@client.location\":" +
                        "{\"country\":\"ES\" }, " +
                    "\"@freelancer.location\":" +
                    "{\"country\":\"ES\" } " +
                "} " +
            "}";

    private static final String jsonRecursive2Level_sqlRestrictions = " ( ( searchRuleCriteria.getMission_duration() <= 30 ) && ( searchRuleCriteria.getCommercialrelation_duration() > 180 ) ) || ( searchRuleCriteria.getCommercialrelation_duration() > 60 ) ";
    private static final String jsonRecursive2Level =
            "{\n" +
                "\"name\":\"recursive 2 level\", " +
                "\"rate\":{\"percent\":15 }, " +
                "\"restrictions\":{\n" +
                    "\"@or\":[\n" +
                        "{\n" +
                            "\"@and\":["+
                                "{"+
                                    "\"@mission.duration\":{"+
                                        "\"lte\":\"1months\""+
                                    "}"+
                                "},"+
                                "{"+
                                    "\"@commercialrelation.duration\":{"+
                                        "\"gt\":\"6months\""+
                                    "}"+
                                "}"+
                            "]"+
                        "}, " +
                        "{\n" +
                            "\"@commercialrelation.duration\":" +
                                "{\"gt\":\"2months\" } " +
                        "} " +
                    "], " +
                    "\"@client.location\":" +
                        "{\"country\":\"FR\" }, " +
                    "\"@freelancer.location\":" +
                    "{\"country\":\"IT\" } " +
                "} " +
            "}";

    //@formatter:on
    @Test
    public void testParsing() {
        final FeeRule feeRuleSimple = convertBodyToFeeRule(jsonSimple);
        assertThat(feeRuleSimple.getName()).isEqualToIgnoringCase("spain or repeat");
        assertThat(feeRuleSimple.getClientLocationCountry()).isEqualToIgnoringCase("ES");
        assertThat(feeRuleSimple.getFreelancerLocationCountry()).isEqualToIgnoringCase("ES");
        assertThat(feeRuleSimple.getSqlRestrictions()).isEqualToIgnoringCase(jsonSimple_sqlRestrictions);
        assertThat(feeRuleSimple.getRate()).isEqualTo(8L);

        final FeeRule feeRuleRecursive2Level = convertBodyToFeeRule(jsonRecursive2Level);
        assertThat(feeRuleRecursive2Level.getName()).isEqualToIgnoringCase("recursive 2 level");
        assertThat(feeRuleRecursive2Level.getClientLocationCountry()).isEqualToIgnoringCase("FR");
        assertThat(feeRuleRecursive2Level.getFreelancerLocationCountry()).isEqualToIgnoringCase("IT");
        assertThat(feeRuleRecursive2Level.getSqlRestrictions()).isEqualToIgnoringCase(jsonRecursive2Level_sqlRestrictions);
        assertThat(feeRuleRecursive2Level.getRate()).isEqualTo(15L);
    }

    @Test
    public void testGetNbDaysFromDuration() {
        assertThat(getNbDaysFromDuration("")).isEqualTo(0L);
        assertThat(getNbDaysFromDuration(null)).isEqualTo(0L);
        assertThat(getNbDaysFromDuration("cdscsdqce")).isEqualTo(0L);
        assertThat(getNbDaysFromDuration("1month")).isEqualTo(30L);
        assertThat(getNbDaysFromDuration("2months")).isEqualTo(60L);
        assertThat(getNbDaysFromDuration("1week")).isEqualTo(7L);
        assertThat(getNbDaysFromDuration("2week")).isEqualTo(14L);
        assertThat(getNbDaysFromDuration("1year")).isEqualTo(365L);
        assertThat(getNbDaysFromDuration("2years")).isEqualTo(730L);
        assertThat(getNbDaysFromDuration("1day")).isEqualTo(1L);
        assertThat(getNbDaysFromDuration("2days")).isEqualTo(2L);
    }

    @Test
    public void testFilterFieldName() {
        assertThat(filterFieldName("")).isEqualTo("");
        assertThat(filterFieldName("csqcsdcqze")).isEqualTo(SEARCH_CRITERIA_INSTANCE_GETTER+"Csqcsdcqze()");
        assertThat(filterFieldName(null)).isEqualTo("");
        assertThat(filterFieldName("@commercialrelation.duration")).isEqualTo(SEARCH_CRITERIA_INSTANCE_GETTER+"Commercialrelation_duration()");
    }

    @Test
    public void testSetCamelCase() {
        assertThat(setCamelCase("")).isEqualTo("");
        assertThat(setCamelCase("csqcsdcqze")).isEqualTo("Csqcsdcqze");
        assertThat(setCamelCase(null)).isEqualTo("");
        assertThat(setCamelCase("@commercialrelation.duration")).isEqualTo("@commercialrelation.duration");
    }

    @Test
    public void testConvertLogicialOperand() {
        assertThat(convertLogicialOperand("")).isEqualTo("");
        assertThat(convertLogicialOperand("csqcsdcqze")).isEqualTo("");
        assertThat(convertLogicialOperand(null)).isEqualTo("");
        assertThat(convertLogicialOperand("@or")).isEqualTo("||");
        assertThat(convertLogicialOperand("@and")).isEqualTo("&&");
    }

    @Test
    public void testConvertOperator() {
        assertThat(convertOperator("")).isEqualTo("");
        assertThat(convertOperator("c<d<dcD")).isEqualTo("");
        assertThat(convertOperator(null)).isEqualTo("");
        assertThat(convertOperator("gt")).isEqualTo(" > ");
        assertThat(convertOperator("lt")).isEqualTo(" < ");
        assertThat(convertOperator("gte")).isEqualTo(" >= ");
        assertThat(convertOperator("lte")).isEqualTo(" <= ");
        assertThat(convertOperator("neq")).isEqualTo(" != ");
        assertThat(convertOperator("eq")).isEqualTo(" == ");
        assertThat(convertOperator("")).isEqualTo("");
    }
}
