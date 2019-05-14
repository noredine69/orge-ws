package com.noredine69.orge.ws.controller;

import com.noredine69.orge.ws.api.FeeApi;
import com.noredine69.orge.ws.model.FeeDto;
import com.noredine69.orge.ws.model.FeeRequestDto;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class FeeController implements FeeApi{

    @ApiOperation(value = "dddd", nickname = "computeFee", notes = "ccccc", response = FeeDto.class, tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "successful operation", response = FeeDto.class) })
    @RequestMapping(value = "/fee",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    public ResponseEntity<FeeDto> computeFee(@ApiParam(value = "", required = true) @Valid @RequestBody FeeRequestDto body) {
        final FeeDto computatedFee = new FeeDto();
        computatedFee.setFees(8);
        computatedFee.setReason("spain or repeat");
        return new ResponseEntity<FeeDto>(computatedFee, HttpStatus.OK);
    }
}
