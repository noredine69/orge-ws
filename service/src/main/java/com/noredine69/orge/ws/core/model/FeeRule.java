package com.noredine69.orge.ws.core.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeRule {
    private long id;
    private String name;
    private long rate;
    private String sqlRestrictions;
    private String freelancerLocationCountry;
    private String clientLocationCountry;
    private long freelancerLocationCountryId;
    private long clientLocationCountryId;
    private boolean isDefault;
}
