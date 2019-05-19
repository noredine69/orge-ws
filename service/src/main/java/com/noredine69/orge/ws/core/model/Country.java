package com.noredine69.orge.ws.core.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Country {
    private String name;
    private String iso2;
    private String iso3;
    private long code;
}
