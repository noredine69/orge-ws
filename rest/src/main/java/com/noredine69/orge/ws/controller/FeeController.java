package com.noredine69.orge.ws.controller;

import com.noredine69.orge.ws.api.FeeApi;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class FeeController implements FeeApi {

    @Autowired
    private GeolocServiceImpl geolocService;

    @ApiOperation(value = "", nickname = "computeFee", notes = "", response = FeeDto.class, tags = {})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "successful operation", response = FeeDto.class)})
    @RequestMapping(value = "/fee", produces = {"application/json"}, consumes = {"application/json"}, method = RequestMethod.POST)
    public ResponseEntity<FeeDto> computeFee(@ApiParam(value = "", required = true) @Valid @RequestBody final FeeRequestDto body) {
        log.debug("client ip location : " + this.geolocService.geolocFromIp(body.getClient().getIp()));
        log.debug("freelancer ip location : " + this.geolocService.geolocFromIp(body.getFreelancer().getIp()));
        final FeeDto computatedFee = new FeeDto();
        computatedFee.setFees(8);
        computatedFee.setReason("spain or repeat");
        return new ResponseEntity<FeeDto>(computatedFee, HttpStatus.OK);
    }
}
