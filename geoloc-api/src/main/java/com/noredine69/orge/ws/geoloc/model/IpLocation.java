package com.noredine69.orge.ws.geoloc.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpLocation {
    private String ip;
    private String country_code;
}
