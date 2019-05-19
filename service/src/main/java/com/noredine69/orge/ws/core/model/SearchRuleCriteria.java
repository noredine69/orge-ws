package com.noredine69.orge.ws.core.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRuleCriteria {
/*
    {
        "client":{
        "ip":"217.127.206.227"
    },
        "freelancer":{
        "ip":"217.127.206.227"
    },
        "mission":{
        "length":"4months"
    },
        "commercialrelation":{
        "firstmission":"2018-04-16 13:24:17.510Z",
                "last_mission":"2018-07-16 14:24:17.510Z"
    }
    }
 (   ( mission_duration > 60 )  and   ( commercialrelation_duration > 60 )  )
    */
    private String clientIp;
    private String freelancerIp;
    private long mission_duration;
    private long commercialrelation_duration;
}
