package com.noredine69.orge.ws.core.service;

import com.noredine69.orge.ws.core.model.Country;
import com.noredine69.orge.ws.core.model.FeeRule;

import java.util.List;

public interface CountryService {
    Country findCountryCode(long code);
    Country findCountryIso2(final String iso2);
    Country findCountryIso3(final String iso3);
    List<Country> findAllCountry();
}
