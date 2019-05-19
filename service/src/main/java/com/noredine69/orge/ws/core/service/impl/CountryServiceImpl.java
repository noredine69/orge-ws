package com.noredine69.orge.ws.core.service.impl;

import com.noredine69.orge.ws.core.mapper.CountryMapper;
import com.noredine69.orge.ws.core.model.Country;
import com.noredine69.orge.ws.core.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CountryServiceImpl implements CountryService{

    private final CountryMapper countryMapper;

    @Autowired
    public CountryServiceImpl(final CountryMapper mapper) {
        this.countryMapper = mapper;
    }

    public Country findCountryCode(long code) {
        return this.countryMapper.findCountryCode(code);
    }

    public Country findCountryIso2(String iso2) {
        return this.countryMapper.findCountryIso2(iso2);
    }

    public Country findCountryIso3(String iso3) {
        return this.countryMapper.findCountryIso3(iso3);
    }

    public List<Country> findAllCountry() {
        return this.countryMapper.findAllCountry();
    }
}
