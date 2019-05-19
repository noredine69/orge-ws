package com.noredine69.orge.ws.converter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converter inspired from spinn3r/artemis-framework :
 * https://github.com/spinn3r/artemis-framework/blob/master/artemis-datetime/src/main/java/com/spinn3r/artemis/datetime/durations/LiberalDurationParser.java
 */
public class DurationConverter {
    private static final String PATTERN_REG_EX = "(?i)^([0-9.]+)((micros|millis|second|minute|hour|day|week|month|year)s?)$";
    public static final String SECOND_KEYWORD = "S";
    private static Pattern PATTERN = Pattern.compile(PATTERN_REG_EX);

    private DurationConverter(){}

    public static Duration parse(String text) {

        Matcher matcher = PATTERN.matcher(text);

        if (matcher.find()) {
            long value = Long.parseLong(matcher.group(1));
            String unit = matcher.group(2).toUpperCase();

            if ( ! unit.endsWith(SECOND_KEYWORD) ) {
                unit = unit + SECOND_KEYWORD;
            }

            try {
                return convertUnitAndValueToDuration(unit, value );
            } catch (UnsupportedTemporalTypeException e) {
                throw new IllegalArgumentException(getIllegalArgumentExceptionMessage(text));
            }
        }
        throw new IllegalArgumentException(getIllegalArgumentExceptionMessage(text));
    }

    private static String getIllegalArgumentExceptionMessage(String argument) {
        return String.format("Impossible to convert %s to ChronoUnit", argument);
    }

    private static Duration convertUnitAndValueToDuration(final String unit, final long value ) {
        //ChronoUnit chronoUnit = ChronoUnit.valueOf(unit);
        ChronoUnitConverterEnum chronoUnitConverterEnum = ChronoUnitConverterEnum.valueOfIgnoreCase(unit);
        final long nbDays =  value*chronoUnitConverterEnum.getNbDaysCoef();
        return Duration.of(nbDays, chronoUnitConverterEnum.getChronoUnit());
    }
}
