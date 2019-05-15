package com.noredine69.orge.ws.geoloc.service;

import com.noredine69.orge.ws.geoloc.model.IpLocation;

public interface GeolocService {

    IpLocation geolocFromIp(String ip);
}
