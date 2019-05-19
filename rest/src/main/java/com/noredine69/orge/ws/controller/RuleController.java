package com.noredine69.orge.ws.controller;

import com.noredine69.orge.ws.api.RuleApi;
import com.noredine69.orge.ws.business.BusinessFeeRuleService;
import com.noredine69.orge.ws.model.RuleDto;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class RuleController implements RuleApi {

    @Autowired
    private BusinessFeeRuleService businessFeeRuleService;

    public ResponseEntity<Void> addRule(@ApiParam(value = "", required = true) @Valid @RequestBody final RuleDto body) {
        try {
            this.businessFeeRuleService.addNewRule(body);
        } catch (final Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
