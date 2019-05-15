package com.noredine69.orge.ws.geoloc.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.noredine69.orge.ws.geoloc.model.IpLocation;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static java.net.HttpURLConnection.HTTP_OK;

@Service("geolocServiceImpl")
@Slf4j
public class GeolocServiceImpl implements  GeolocService{

    @Value("${geoloc.provider.url}")
    private String providerUrl;
    @Value("${geoloc.provider.access_key}")
    private String providerAccessKey;
    private WebTarget webTarget;
    private Client client;
    private JacksonJsonProvider jacksonJsonProvider;
    private final String ACCESS_KEY = "access_key";
    private final String FIELDS_KEY="fields";
    private final String FIELDS_VALUE="ip,country_code";
    private final String OUTPUT_KEY="output";
    private final String OUTPUT_VALUE="json";

    @PostConstruct
    public void init() {
        this.jacksonJsonProvider = new JacksonJaxbJsonProvider();
        this.jacksonJsonProvider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final ClientConfig clientConfig = new ClientConfig(this.jacksonJsonProvider);
        this.client = this.getClientBuilder().withConfig(clientConfig).build();
        this.webTarget = this.client.target(providerUrl);
    }

    ClientBuilder getClientBuilder() {
        return ClientBuilder.newBuilder();
    }

    public IpLocation geolocFromIp(String ip) {
        IpLocation ipLocation = null;
        Response response = this.webTarget.path(ip).queryParam(ACCESS_KEY, providerAccessKey).queryParam(FIELDS_KEY, FIELDS_VALUE).queryParam(OUTPUT_KEY, OUTPUT_VALUE).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == HTTP_OK) {
            ipLocation= response.readEntity(IpLocation.class);
            //log.debug("Receive json object from provider {}", ipLocation);
            try {
                response.close();
            } catch (final Exception e) {
                //log.error("Exception unserializing object from json response {}", e);
            }
        }
        return ipLocation;
    }


}
