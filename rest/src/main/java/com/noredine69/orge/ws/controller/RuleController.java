package com.noredine69.orge.ws.controller;

import com.noredine69.orge.ws.api.RuleApi;
import com.noredine69.orge.ws.model.RuleDto;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RuleController implements RuleApi {

    public ResponseEntity<Void> addRule(@ApiParam(value = "", required = true) @Valid @RequestBody RuleDto body) {
        return null;
    }
}
