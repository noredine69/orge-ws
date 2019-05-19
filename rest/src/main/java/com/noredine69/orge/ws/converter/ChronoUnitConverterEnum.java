package com.noredine69.orge.ws.converter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.temporal.ChronoUnit;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum ChronoUnitConverterEnum {

    DAYS(1, ChronoUnit.DAYS),
    WEEKS(7, ChronoUnit.DAYS),
    MONTHS(30, ChronoUnit.DAYS),
    YEARS(365, ChronoUnit.DAYS);

    private long nbDaysCoef;
    private ChronoUnit chronoUnit;

    static ChronoUnitConverterEnum valueOfIgnoreCase(String name) {
        if(StringUtils.isNotBlank(name)) {
            return valueOf(name.toUpperCase());
        }
        throw new IllegalArgumentException( String.format("Impossible to convert %s to ChronoUnit", name));
    }
}
