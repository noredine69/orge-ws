package com.noredine69.orge.ws.controller;

import com.noredine69.orge.ws.api.RuleApi;
import com.noredine69.orge.ws.model.RuleDto;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@Slf4j
@RestController
public class RuleController implements RuleApi {

    public ResponseEntity<Void> addRule(@ApiParam(value = "", required = true) @Valid @RequestBody final RuleDto body) {
        final Object restrictionObject = body.getRestrictions();
        if (restrictionObject instanceof LinkedHashMap) {
            final LinkedHashMap restrictionsMap = (LinkedHashMap) restrictionObject;
            for (final Object keyObj : restrictionsMap.keySet()) {
                if (keyObj instanceof String) {
                    final String key = (String) keyObj;
                    final Object value = restrictionsMap.get(key);
                    if (value instanceof LinkedHashMap) {
                        final LinkedHashMap maps = (LinkedHashMap) value;
                        //log.debug("restrictions : " + key + "-" + maps.keySet() + "-" + maps.values());
                    } else if (value instanceof ArrayList) {
                        final ArrayList list = (ArrayList) value;
                        //log.debug("restrictions : " + key + "-" + list.get(0) + "-" + list.get(1));
                    }
                }
            }
        }
        return null;
    }
}
