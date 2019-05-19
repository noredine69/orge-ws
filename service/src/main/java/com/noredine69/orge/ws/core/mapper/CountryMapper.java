package com.noredine69.orge.ws.core.mapper;


import com.noredine69.orge.ws.core.model.Country;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CountryMapper {
    Country findCountryCode(@Param("code")long code);
    Country findCountryIso2(@Param("iso2")final String iso2);
    Country findCountryIso3(@Param("iso3")final String iso3);
    List<Country> findAllCountry();
}
