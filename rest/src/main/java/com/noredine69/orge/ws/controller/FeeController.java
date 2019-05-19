package com.noredine69.orge.ws.controller;

import com.noredine69.orge.ws.api.FeeApi;
import com.noredine69.orge.ws.business.impl.BusinessRateServiceImpl;
import com.noredine69.orge.ws.core.service.FeeRuleService;
import com.noredine69.orge.ws.geoloc.service.GeolocServiceImpl;
import com.noredine69.orge.ws.model.FeeDto;
import com.noredine69.orge.ws.model.FeeRequestDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.incrementer.SybaseAnywhereMaxValueIncrementer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class FeeController implements FeeApi {

    @Autowired
    private BusinessRateServiceImpl businessRateService;

    public ResponseEntity<FeeDto> computeFee(@ApiParam(value = "", required = true) @Valid @RequestBody final FeeRequestDto body) {
        try {
            FeeDto feeDto = this.businessRateService.searchRate(body);
            return new ResponseEntity<>(feeDto, HttpStatus.OK);
        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
